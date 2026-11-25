package br.com.agrosafe.backend.colheita.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelamentoColheitaRequest(

        @NotBlank(message = "O motivo do cancelamento é obrigatório")
        @Size(max = 500, message = "O motivo deve ter no máximo 500 caracteres")
        String motivo

) {
}
