package br.com.agrosafe.backend.colheita;

import br.com.agrosafe.backend.colheita.dto.CancelamentoColheitaRequest;
import br.com.agrosafe.backend.colheita.dto.CancelamentoColheitaResponse;
import br.com.agrosafe.backend.colheita.dto.ColheitaRequest;
import br.com.agrosafe.backend.colheita.dto.ColheitaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gestor e Operador podem registrar colheitas (basta estar autenticado).
 * Somente o Gestor cancela uma colheita (US-22).
 */
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

    // SCRUM-63 (US-22): somente Gestor pode cancelar.
    @PatchMapping("/{id}/cancelamento")
    @PreAuthorize("hasRole('GESTOR')")
    public CancelamentoColheitaResponse cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelamentoColheitaRequest request,
            Authentication authentication) {
        return service.cancelar(id, request.motivo(), authentication.getName());
    }
}
