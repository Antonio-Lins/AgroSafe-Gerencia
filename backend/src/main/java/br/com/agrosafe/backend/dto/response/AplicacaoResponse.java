package br.com.agrosafe.backend.dto.response;

import br.com.agrosafe.backend.model.Aplicacao;
import java.time.LocalDate;

/**
 * "dataLiberacaoCalculada" é a liberação desta aplicação (RN02).
 * "dataLiberacaoTalhao" é a data de liberação do talhão depois do registro (RN03).
 */
public record AplicacaoResponse(
        Long id,
        Long talhaoId,
        String talhaoCodigo,
        Long defensivoId,
        String defensivoNome,
        Long usuarioId,
        LocalDate dataAplicacao,
        LocalDate dataLiberacaoCalculada,
        LocalDate dataLiberacaoTalhao,
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
                aplicacao.getTalhao().getDataLiberacao(),
                aplicacao.isCancelada());
    }
}