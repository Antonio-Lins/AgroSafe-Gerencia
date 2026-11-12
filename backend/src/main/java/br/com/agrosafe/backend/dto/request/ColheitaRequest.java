
package br.com.agrosafe.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ColheitaRequest(

        @NotNull(message = "O talhão é obrigatório")
        Long talhaoId,

        @NotNull(message = "A data da colheita é obrigatória")
        LocalDate dataColheita,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        BigDecimal quantidade

) {
}
