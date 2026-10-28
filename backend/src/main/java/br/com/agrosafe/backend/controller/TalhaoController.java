package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.request.TalhaoRequest;
import br.com.agrosafe.backend.dto.response.TalhaoResponse;
import br.com.agrosafe.backend.service.TalhaoService;
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

/**
 * Leitura: qualquer usuário autenticado. Escrita: somente Gestor (RF01).
 * O @PreAuthorize só vale se a configuração de segurança tiver @EnableMethodSecurity
 * e o perfil GESTOR virar a autoridade ROLE_GESTOR (US-03).
 */

@RestController
@RequestMapping("/talhoes")
public class TalhaoController {
    private final TalhaoService service;

    public TalhaoController(TalhaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TalhaoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TalhaoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GESTOR')")
    public TalhaoResponse criar(@Valid @RequestBody TalhaoRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR')")
    public TalhaoResponse atualizar(@PathVariable Long id, @Valid @RequestBody TalhaoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('GESTOR')")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
