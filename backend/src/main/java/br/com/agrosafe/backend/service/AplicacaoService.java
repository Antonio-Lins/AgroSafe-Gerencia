package br.com.agrosafe.backend.service;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrosafe.backend.dto.request.AplicacaoRequest;
import br.com.agrosafe.backend.dto.response.AplicacaoResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.model.Aplicacao;
import br.com.agrosafe.backend.model.Defensivo;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.AplicacaoRepository;
import br.com.agrosafe.backend.repository.DefensivoRepository;
import br.com.agrosafe.backend.repository.TalhaoRepository;
import br.com.agrosafe.backend.repository.UsuarioRepository;

@Service
public class AplicacaoService {

    private final AplicacaoRepository aplicacaoRepository;
    private final TalhaoRepository talhaoRepository;
    private final DefensivoRepository defensivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final Clock clock;

    public AplicacaoService(AplicacaoRepository aplicacaoRepository,
                            TalhaoRepository talhaoRepository,
                            DefensivoRepository defensivoRepository,
                            UsuarioRepository usuarioRepository,
                            Clock clock) {
        this.aplicacaoRepository = aplicacaoRepository;
        this.talhaoRepository = talhaoRepository;
        this.defensivoRepository = defensivoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clock = clock;
    }

    /**
     * US-09 (RF02, RN02): registra a aplicação, calcula e grava a data de liberação e associa
     * a aplicação ao usuário autenticado.
     * US-10 (RN03): atualiza a data de liberação do talhão, que passa a ser a maior entre as
     * suas aplicações.
     *
     * A atualização da data de liberação do talhão (RN03) é da US-10 e deve entrar neste
     * mesmo método, dentro da mesma transação (driver D3). A data da aplicação não pode
     * ser futura (RN07, US-11): a validação ocorre antes de qualquer consulta ou gravação.
     */
    @Transactional
    public AplicacaoResponse registrar(AplicacaoRequest request, String emailUsuario) {
        // RN07: aplicação com data futura é recusada (hoje é permitido)
        if (request.dataAplicacao().isAfter(LocalDate.now(clock))) {
            throw new BusinessException("A data da aplicação não pode ser futura.");
        }
        Talhao talhao = talhaoRepository.findById(request.talhaoId())
                .orElseThrow(() -> referenciaInexistente("Talhão não encontrado."));
        Defensivo defensivo = defensivoRepository.findById(request.defensivoId())
                .orElseThrow(() -> referenciaInexistente("Defensivo não encontrado."));
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailUsuario)
                .orElseThrow(() -> new BusinessException(
                        "Usuário autenticado não encontrado.", HttpStatus.UNAUTHORIZED));

        LocalDate dataLiberacao = defensivo.calcularDataLiberacao(request.dataAplicacao());
        Aplicacao aplicacao = new Aplicacao(talhao, defensivo, usuario,
                request.dataAplicacao(), dataLiberacao);
        Aplicacao salva = aplicacaoRepository.save(aplicacao);

        // RN03: a aplicação e a data do talhão são gravadas na mesma transação.
        // Se qualquer uma falhar, nenhuma das duas alterações vale (driver D3).
        talhao.atualizarLiberacaoSeMaior(dataLiberacao);
        talhaoRepository.save(talhao);

        return AplicacaoResponse.de(salva);
    }

    private BusinessException referenciaInexistente(String mensagem) {
        return new BusinessException(mensagem, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}