
package br.com.agrosafe.backend.aplicacao.dto;

import br.com.agrosafe.backend.aplicacao.Aplicacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CancelamentoResponse(
        Long aplicacaoId,
        Long talhaoId,
        boolean cancelada,
        String motivoCancelamento,
        Long canceladaPorId,
        LocalDateTime canceladaEm,
        LocalDate dataLiberacaoTalhao
) {

    public static CancelamentoResponse de(Aplicacao aplicacao) {
        return new CancelamentoResponse(
                aplicacao.getId(),
                aplicacao.getTalhao().getId(),
                aplicacao.isCancelada(),
                aplicacao.getMotivoCancelamento(),
                aplicacao.getCanceladaPor().getId(),
                aplicacao.getCanceladaEm(),
                aplicacao.getTalhao().getDataLiberacao()
        );
    }
}

