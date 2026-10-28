package br.com.agrosafe.backend.dto.response;

import java.time.Instant;

/** Resposta do endpoint de teste: prova que a API está respondendo. */
public record PingResponse(String status, String aplicacao, Instant timestamp) {
}