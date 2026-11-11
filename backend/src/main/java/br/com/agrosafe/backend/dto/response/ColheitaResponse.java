package br.com.agrosafe.backend.dto.response;

import br.com.agrosafe.backend.model.Colheita;
import java.time.LocalDate;

public record ColheitaResponse(
        Long id,
        Long talhaoId,
        String talhaoCodigo,
        Long usuarioId,
        LocalDate dataColheita) {

    public static ColheitaResponse de(Colheita colheita) {
        return new ColheitaResponse(
                colheita.getId(),
                colheita.getTalhao().getId(),
                colheita.getTalhao().getCodigo(),
                colheita.getUsuario().getId(),
                colheita.getDataColheita());
    }
}
