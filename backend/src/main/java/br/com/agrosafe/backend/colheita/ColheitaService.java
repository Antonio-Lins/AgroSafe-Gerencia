
package br.com.agrosafe.backend.colheita;

import br.com.agrosafe.backend.colheita.dto.CancelamentoColheitaResponse;
import br.com.agrosafe.backend.colheita.dto.ColheitaRequest;
import br.com.agrosafe.backend.colheita.dto.ColheitaResponse;
import br.com.agrosafe.backend.shared.exception.BusinessException;
import br.com.agrosafe.backend.shared.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.talhao.EstadoTalhao;
import br.com.agrosafe.backend.talhao.Talhao;
import br.com.agrosafe.backend.usuario.Perfil;
import br.com.agrosafe.backend.usuario.Usuario;
import br.com.agrosafe.backend.talhao.TalhaoRepository;
import br.com.agrosafe.backend.usuario.UsuarioRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColheitaService {

    private static final DateTimeFormatter FORMATO_BR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ColheitaRepository colheitaRepository;
    private final TalhaoRepository talhaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final Clock clock;

    public ColheitaService(
            ColheitaRepository colheitaRepository,
            TalhaoRepository talhaoRepository,
            UsuarioRepository usuarioRepository,
            Clock clock) {

        this.colheitaRepository = colheitaRepository;
        this.talhaoRepository = talhaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clock = clock;
    }

    @Transactional
    public ColheitaResponse registrar(
            ColheitaRequest request,
            String emailUsuario) {

        // 1. Buscar o talhão
        Talhao talhao = talhaoRepository
                .findById(request.talhaoId())
                .orElseThrow(() ->
                        new BusinessException("Talhão não encontrado.")
                );

        // 2. Identificar o usuário autenticado
        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(emailUsuario)
                .orElseThrow(() ->
                        new BusinessException(
                                "Usuário autenticado não encontrado.",
                                HttpStatus.UNAUTHORIZED
                        )
                );

        // 3. Verificar a quarentena
        LocalDate hoje = LocalDate.now(clock);

        if (talhao.estadoEm(hoje) == EstadoTalhao.EM_QUARENTENA) {
            throw new BusinessException(
                    "Talhão em quarentena. Liberação em "
                            + talhao.getDataLiberacao().format(FORMATO_BR)
                            + ".",
                    HttpStatus.CONFLICT
            );
        }

        // 4. Validar os dados da colheita
        if (request.dataColheita() == null
                || request.quantidade() == null
                || request.quantidade().signum() <= 0) {

            throw new BusinessException(
                    "Data e quantidade maior que zero são obrigatórias.",
                    HttpStatus.BAD_REQUEST
            );
        }

        // 5. Criar e salvar o registro
        Colheita colheita = new Colheita(
                talhao,
                usuario,
                request.dataColheita(),
                request.quantidade()
        );

        Colheita salva = colheitaRepository.save(colheita);

        return ColheitaResponse.de(salva);
    }

    /**
     * US-22: cancela a colheita com um motivo obrigatório.
     * O registro original é preservado (nada é excluído) e ficam gravados o motivo,
     * o responsável e o momento do cancelamento. Somente o Gestor pode cancelar.
     */
    @Transactional
    public CancelamentoColheitaResponse cancelar(
            Long colheitaId,
            String motivo,
            String emailUsuario) {

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
                    "Somente o Gestor pode cancelar colheitas.",
                    HttpStatus.FORBIDDEN
            );
        }

        Colheita colheita = colheitaRepository.findById(colheitaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Colheita não encontrada."
                ));

        if (colheita.isCancelada()) {
            throw new BusinessException(
                    "Esta colheita já está cancelada.",
                    HttpStatus.CONFLICT
            );
        }

        colheita.cancelar(
                motivoTratado,
                responsavel,
                LocalDateTime.now(clock)
        );

        Colheita salva = colheitaRepository.save(colheita);

        return CancelamentoColheitaResponse.de(salva);
    }
}
