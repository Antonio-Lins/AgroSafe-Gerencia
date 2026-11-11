package br.com.agrosafe.backend.service;

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
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AplicacaoService {

    private final AplicacaoRepository aplicacaoRepository;
    private final TalhaoRepository talhaoRepository;
    private final DefensivoRepository defensivoRepository;
    private final UsuarioRepository usuarioRepository;

    public AplicacaoService(AplicacaoRepository aplicacaoRepository,
                            TalhaoRepository talhaoRepository,
                            DefensivoRepository defensivoRepository,
                            UsuarioRepository usuarioRepository) {
        this.aplicacaoRepository = aplicacaoRepository;
        this.talhaoRepository = talhaoRepository;
        this.defensivoRepository = defensivoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * US-09 (RF02, RN02): registra a aplicação, calcula e grava a data de liberação
     * e associa a aplicação ao usuário autenticado.
     *
     * A atualização da data de liberação do talhão (RN03) é da US-10 e deve entrar neste
     * mesmo método, dentro da mesma transação (driver D3). A rejeição de data futura (RN07)
     * é da US-11.
     */
    @Transactional
    public AplicacaoResponse registrar(AplicacaoRequest request, String emailUsuario) {
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

        return AplicacaoResponse.de(aplicacaoRepository.save(aplicacao));
    }

    private BusinessException referenciaInexistente(String mensagem) {
        return new BusinessException(mensagem, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}