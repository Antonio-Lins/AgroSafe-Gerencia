package br.com.agrosafe.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TalhaoRequest(
        @NotBlank(message = "O código é obrigatório") String codigo,
        @NotNull(message = "Os hectares são obrigatórios")
        @Positive(message = "Os hectares devem ser maiores que zero") BigDecimal hectares,
        @NotBlank(message = "A cultura é obrigatória") String cultura
) {
}
