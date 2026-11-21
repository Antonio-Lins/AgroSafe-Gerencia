
package br.com.agrosafe.backend.colheita;

import br.com.agrosafe.backend.colheita.dto.ColheitaRequest;
import br.com.agrosafe.backend.colheita.dto.ColheitaResponse;
import br.com.agrosafe.backend.shared.exception.BusinessException;
import br.com.agrosafe.backend.talhao.EstadoTalhao;
import br.com.agrosafe.backend.talhao.Talhao;
import br.com.agrosafe.backend.usuario.Usuario;
import br.com.agrosafe.backend.talhao.TalhaoRepository;
import br.com.agrosafe.backend.usuario.UsuarioRepository;

import java.time.Clock;
import java.time.LocalDate;
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
}
