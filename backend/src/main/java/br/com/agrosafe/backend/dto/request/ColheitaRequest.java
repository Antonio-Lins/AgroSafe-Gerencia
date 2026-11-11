package br.com.agrosafe.backend.dto.request;

import jakarta.validation.constraints.NotNull;

/** Corpo do POST /colheitas. A colheita é registrada na data de hoje. */
public record ColheitaRequest(
        @NotNull(message = "O talhão é obrigatório") Long talhaoId) {
}
