package br.com.agrosafe.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/** Corpo do POST /aplicacoes. A data é enviada no formato AAAA-MM-DD. */
public record AplicacaoRequest(
        @NotNull(message = "O talhão é obrigatório") Long talhaoId,
        @NotNull(message = "O defensivo é obrigatório") Long defensivoId,
        @NotNull(message = "A data da aplicação é obrigatória") LocalDate dataAplicacao) {
}