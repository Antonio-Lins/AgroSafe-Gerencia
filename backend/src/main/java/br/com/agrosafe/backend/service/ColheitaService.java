package br.com.agrosafe.backend.service;

import br.com.agrosafe.backend.dto.request.ColheitaRequest;
import br.com.agrosafe.backend.dto.response.ColheitaResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.model.Colheita;
import br.com.agrosafe.backend.model.EstadoTalhao;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.ColheitaRepository;
import br.com.agrosafe.backend.repository.TalhaoRepository;
import br.com.agrosafe.backend.repository.UsuarioRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColheitaService {

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ColheitaRepository colheitaRepository;
    private final TalhaoRepository talhaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final Clock clock;

    public ColheitaService(ColheitaRepository colheitaRepository,
                           TalhaoRepository talhaoRepository,
                           UsuarioRepository usuarioRepository,
                           Clock clock) {
        this.colheitaRepository = colheitaRepository;
        this.talhaoRepository = talhaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clock = clock;
    }

    /**
     * Registra a colheita de um talhão na data de hoje.
     *
     * US-13 (RN01, RN04): a colheita é recusada com 409 enquanto o talhão estiver em
     * quarentena. No dia da liberação o talhão já está livre (RN04), e nada é gravado
     * quando a colheita é recusada.
     */
    @Transactional
    public ColheitaResponse registrar(ColheitaRequest request, String emailUsuario) {
        Talhao talhao = talhaoRepository.findById(request.talhaoId())
                .orElseThrow(() -> new BusinessException("Talhão não encontrado."));
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailUsuario)
                .orElseThrow(() -> new BusinessException(
                        "Usuário autenticado não encontrado.", HttpStatus.UNAUTHORIZED));

        LocalDate hoje = LocalDate.now(clock);
        if (talhao.estadoEm(hoje) == EstadoTalhao.EM_QUARENTENA) {
            throw new BusinessException(
                    "Talhão em quarentena. Liberação em "
                            + talhao.getDataLiberacao().format(FORMATO_BR) + ".",
                    HttpStatus.CONFLICT);
        }

        return ColheitaResponse.de(colheitaRepository.save(new Colheita(talhao, usuario, hoje)));
    }
}
