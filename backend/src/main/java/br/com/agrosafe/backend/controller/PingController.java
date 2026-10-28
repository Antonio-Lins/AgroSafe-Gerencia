package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.response.PingResponse;
import br.com.agrosafe.backend.service.PingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint de teste usado pelo front-end React para confirmar a integração (US-01). */
@RestController
@RequestMapping("/api")
public class PingController {

    private final PingService pingService;

    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("/ping")
    public PingResponse ping() {
        return pingService.ping();
    }
}