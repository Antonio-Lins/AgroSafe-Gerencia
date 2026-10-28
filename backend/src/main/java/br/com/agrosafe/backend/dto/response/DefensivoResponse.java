package br.com.agrosafe.backend.dto.response;

import br.com.agrosafe.backend.model.Defensivo;

public record DefensivoResponse(
        Long id,
        String nomeComercial,
        String principioAtivo,
        Integer diasCarencia
) {
    public static DefensivoResponse de(Defensivo defensivo) {
        return new DefensivoResponse(
                defensivo.getId(),
                defensivo.getNomeComercial(),
                defensivo.getPrincipioAtivo(),
                defensivo.getDiasCarencia());
    }
}
