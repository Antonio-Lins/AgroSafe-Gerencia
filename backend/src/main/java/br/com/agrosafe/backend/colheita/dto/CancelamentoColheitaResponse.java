package br.com.agrosafe.backend.colheita.dto;

import br.com.agrosafe.backend.colheita.Colheita;
import java.time.LocalDateTime;

public record CancelamentoColheitaResponse(
        Long colheitaId,
        Long talhaoId,
        boolean cancelada,
        String motivoCancelamento,
        Long canceladaPorId,
        LocalDateTime canceladaEm
) {

    public static CancelamentoColheitaResponse de(Colheita colheita) {
        return new CancelamentoColheitaResponse(
                colheita.getId(),
                colheita.getTalhao().getId(),
                colheita.isCancelada(),
                colheita.getMotivoCancelamento(),
                colheita.getCanceladaPor().getId(),
                colheita.getCanceladaEm()
        );
    }
}
