package br.com.agrosafe.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DefensivoRequest(
        @NotBlank(message = "O nome comercial é obrigatório") String nomeComercial,
        @NotBlank(message = "O princípio ativo é obrigatório") String principioAtivo,
        @NotNull(message = "A carência é obrigatória")
        @Min(value = 1, message = "A carência deve ser um inteiro maior que zero") Integer diasCarencia) {}
