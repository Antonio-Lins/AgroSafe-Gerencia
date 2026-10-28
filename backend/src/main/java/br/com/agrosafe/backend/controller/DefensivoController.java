package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.request.DefensivoRequest;
import br.com.agrosafe.backend.dto.response.DefensivoResponse;
import br.com.agrosafe.backend.service.DefensivoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/defensivos")
public class DefensivoController {

    private final DefensivoService service;

    public DefensivoController(DefensivoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DefensivoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public DefensivoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GESTOR')")
    public DefensivoResponse criar(@Valid @RequestBody DefensivoRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR')")
    public DefensivoResponse atualizar(@PathVariable Long id, @Valid @RequestBody DefensivoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('GESTOR')")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
