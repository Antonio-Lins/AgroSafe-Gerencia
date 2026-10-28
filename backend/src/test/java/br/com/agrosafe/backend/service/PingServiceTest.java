package br.com.agrosafe.backend.service;

import br.com.agrosafe.backend.dto.response.PingResponse;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PingServiceTest {

    private static final Instant AGORA = Instant.parse("2026-10-28T12:00:00Z");

    @Test
    void retornaStatusOkComNomeDaAplicacaoEHoraDoRelogio() {
        PingService service = new PingService(Clock.fixed(AGORA, ZoneOffset.UTC), "agrosafe-backend");

        PingResponse resposta = service.ping();

        assertEquals("ok", resposta.status());
        assertEquals("agrosafe-backend", resposta.aplicacao());
        assertEquals(AGORA, resposta.timestamp());
    }
}