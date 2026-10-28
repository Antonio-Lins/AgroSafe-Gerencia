package br.com.agrosafe.backend.service;

import br.com.agrosafe.backend.dto.response.PingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class PingService {

    private final Clock clock;
    private final String nomeAplicacao;

    public PingService(Clock clock,
                       @Value("${spring.application.name:agrosafe-backend}") String nomeAplicacao) {
        this.clock = clock;
        this.nomeAplicacao = nomeAplicacao;
    }

    public PingResponse ping() {
        return new PingResponse("ok", nomeAplicacao, Instant.now(clock));
    }
}