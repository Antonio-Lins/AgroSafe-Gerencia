package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.request.AplicacaoRequest;
import br.com.agrosafe.backend.dto.response.AplicacaoResponse;
import br.com.agrosafe.backend.service.AplicacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Gestor e Operador podem registrar aplicações (basta estar autenticado). */
@RestController
@RequestMapping("/aplicacoes")
public class AplicacaoController {

    private final AplicacaoService service;

    public AplicacaoController(AplicacaoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AplicacaoResponse registrar(@Valid @RequestBody AplicacaoRequest request,
                                       Authentication authentication) {
        return service.registrar(request, authentication.getName());
    }
}
