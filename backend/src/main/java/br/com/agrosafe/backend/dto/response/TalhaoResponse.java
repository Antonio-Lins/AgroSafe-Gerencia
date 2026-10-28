package br.com.agrosafe.backend.dto.response;

import br.com.agrosafe.backend.model.EstadoTalhao;
import br.com.agrosafe.backend.model.Talhao;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TalhaoResponse(
        Long id,
        String codigo,
        BigDecimal hectares,
        String cultura,
        LocalDate dataLiberacao,
        EstadoTalhao estado
) {
    public static TalhaoResponse de(Talhao talhao, LocalDate hoje) {
        return new TalhaoResponse(
                talhao.getId(),
                talhao.getCodigo(),
                talhao.getHectares(),
                talhao.getCultura(),
                talhao.getDataLiberacao(),
                talhao.estadoEm(hoje));
    }
}
