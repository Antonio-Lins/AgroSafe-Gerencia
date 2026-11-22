package br.com.agrosafe.backend.health;

import br.com.agrosafe.backend.health.dto.PingResponse;
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