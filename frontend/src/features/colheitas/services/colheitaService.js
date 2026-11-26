import { apiFetch } from '../../../shared/services/api.js';

import {
    ERRO_DE_REDE,
    extrairErro,
} from '../../../shared/services/erros.js';

export async function registrarColheita(colheita) {
    let resposta;

    try {
        resposta = await apiFetch('/colheitas', {
            method: 'POST',
            body: JSON.stringify(colheita),
        });
    } catch {
        return {
            ok: false,
            ...ERRO_DE_REDE,
        };
    }

    if (!resposta.ok) {
        return {
            ok: false,
            ...(await extrairErro(resposta)),
        };
    }

    return {
        ok: true,
        dados: await resposta.json(),
    };
}