package br.com.agrosafe.backend.aplicacao;

import br.com.agrosafe.backend.aplicacao.dto.AplicacaoRequest;
import br.com.agrosafe.backend.aplicacao.dto.AplicacaoResponse;
import br.com.agrosafe.backend.aplicacao.dto.CancelamentoRequest;
import br.com.agrosafe.backend.aplicacao.dto.CancelamentoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    // SCRUM-61 + SCRUM-62: somente Gestor pode cancelar.
    @PatchMapping("/{id}/cancelamento")
    @PreAuthorize("hasRole('GESTOR')")
    public CancelamentoResponse cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelamentoRequest request,
            Authentication authentication
    ) {
        return service.cancelar(
                id,
                request.motivo(),
                authentication.getName()
        );
    }
}
