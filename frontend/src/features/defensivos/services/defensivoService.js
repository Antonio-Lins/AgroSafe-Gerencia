
import { apiFetch } from '../../../shared/services/api.js';
import { ERRO_DE_REDE, extrairErro } from '../../../shared/services/erros.js';

async function chamar(url, opcoes) {
    let resposta;

    try {
        resposta = await apiFetch(url, opcoes);
    } catch {
        return { ok: false, ...ERRO_DE_REDE };
    }

    if (!resposta.ok) {
        return {
            ok: false,
            ...(await extrairErro(resposta)),
        };
    }

    if (resposta.status === 204) {
        return { ok: true, dados: null };
    }

    try {
        return {
            ok: true,
            dados: await resposta.json(),
        };
    } catch {
        return {
            ok: false,
            status: resposta.status,
            mensagem: 'A resposta do servidor é inválida.',
            campos: {},
        };
    }
}

export function listarDefensivos() {
    return chamar('/defensivos');
}

export function buscarDefensivo(id) {
    return chamar(`/defensivos/${id}`);
}

export function criarDefensivo(defensivo) {
    return chamar('/defensivos', {
        method: 'POST',
        body: JSON.stringify(defensivo),
    });
}

export function atualizarDefensivo(id, defensivo) {
    return chamar(`/defensivos/${id}`, {
        method: 'PUT',
        body: JSON.stringify(defensivo),
    });
}

export function removerDefensivo(id) {
    return chamar(`/defensivos/${id}`, {
        method: 'DELETE',
    });
}
