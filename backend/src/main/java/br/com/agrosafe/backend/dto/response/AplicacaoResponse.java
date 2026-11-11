package br.com.agrosafe.backend.dto.response;

import br.com.agrosafe.backend.model.Aplicacao;
import java.time.LocalDate;

public record AplicacaoResponse(
        Long id,
        Long talhaoId,
        String talhaoCodigo,
        Long defensivoId,
        String defensivoNome,
        Long usuarioId,
        LocalDate dataAplicacao,
        LocalDate dataLiberacaoCalculada,
        boolean cancelada) {

    public static AplicacaoResponse de(Aplicacao aplicacao) {
        return new AplicacaoResponse(
                aplicacao.getId(),
                aplicacao.getTalhao().getId(),
                aplicacao.getTalhao().getCodigo(),
                aplicacao.getDefensivo().getId(),
                aplicacao.getDefensivo().getNomeComercial(),
                aplicacao.getUsuario().getId(),
                aplicacao.getDataAplicacao(),
                aplicacao.getDataLiberacaoCalculada(),
                aplicacao.isCancelada());
    }
}