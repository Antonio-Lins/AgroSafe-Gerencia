package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.request.ColheitaRequest;
import br.com.agrosafe.backend.dto.response.ColheitaResponse;
import br.com.agrosafe.backend.service.ColheitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Gestor e Operador podem registrar colheitas (basta estar autenticado). */
@RestController
@RequestMapping("/colheitas")
public class ColheitaController {

    private final ColheitaService service;

    public ColheitaController(ColheitaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ColheitaResponse registrar(@Valid @RequestBody ColheitaRequest request,
                                      Authentication authentication) {
        return service.registrar(request, authentication.getName());
    }
}
