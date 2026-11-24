package br.com.agrosafe.backend.aplicacao;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.agrosafe.backend.aplicacao.dto.CancelamentoResponse;
import br.com.agrosafe.backend.shared.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.usuario.Perfil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agrosafe.backend.aplicacao.dto.AplicacaoRequest;
import br.com.agrosafe.backend.aplicacao.dto.AplicacaoResponse;
import br.com.agrosafe.backend.shared.exception.BusinessException;
import br.com.agrosafe.backend.defensivo.Defensivo;
import br.com.agrosafe.backend.talhao.Talhao;
import br.com.agrosafe.backend.usuario.Usuario;
import br.com.agrosafe.backend.defensivo.DefensivoRepository;
import br.com.agrosafe.backend.talhao.TalhaoRepository;
import br.com.agrosafe.backend.usuario.UsuarioRepository;

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

    // SCRUM-61 (US-20): cancelar aplicação com motivo.
    // SCRUM-62 (US-21): recalcular liberação após cancelamento.
    @Transactional
    public CancelamentoResponse cancelar(
            Long aplicacaoId,
            String motivo,
            String emailUsuario
    ) {

        if (motivo == null || motivo.isBlank()) {
            throw new BusinessException(
                    "O motivo do cancelamento é obrigatório.",
                    HttpStatus.BAD_REQUEST
            );
        }

        String motivoTratado = motivo.strip();

        if (motivoTratado.length() > 500) {
            throw new BusinessException(
                    "O motivo deve ter no máximo 500 caracteres.",
                    HttpStatus.BAD_REQUEST
            );
        }

        Usuario responsavel = usuarioRepository
                .findByEmailIgnoreCase(emailUsuario)
                .orElseThrow(() -> new BusinessException(
                        "Usuário autenticado não encontrado.",
                        HttpStatus.UNAUTHORIZED
                ));

        if (responsavel.getPerfil() != Perfil.GESTOR) {
            throw new BusinessException(
                    "Somente o Gestor pode cancelar aplicações.",
                    HttpStatus.FORBIDDEN
            );
        }

        Aplicacao aplicacao = aplicacaoRepository.findById(aplicacaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aplicação não encontrada."
                ));

        if (aplicacao.isCancelada()) {
            throw new BusinessException(
                    "Esta aplicação já está cancelada.",
                    HttpStatus.CONFLICT
            );
        }

        // US-20: preserva o registro original e grava a auditoria.
        aplicacao.cancelar(
                motivoTratado,
                responsavel,
                LocalDateTime.now(clock)
        );

        // Garante que o cancelamento seja enviado ao banco antes
        // da consulta que considera apenas aplicações ativas.
        aplicacaoRepository.saveAndFlush(aplicacao);

        Talhao talhao = aplicacao.getTalhao();

        // US-21: a data pode diminuir ou se tornar nula.
        LocalDate novaLiberacao =
                aplicacaoRepository.buscarMaiorDataLiberacaoAtiva(
                        talhao.getId()
                );

        talhao.setDataLiberacao(novaLiberacao);
        talhaoRepository.save(talhao);

        return CancelamentoResponse.de(aplicacao);
    }

    private BusinessException referenciaInexistente(String mensagem) {
        return new BusinessException(mensagem, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}