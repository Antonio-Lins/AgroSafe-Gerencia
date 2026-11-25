
import { apiFetch } from '../../../shared/services/api.js';
import { ERRO_DE_REDE, extrairErro } from '../../../shared/services/erros.js';

export async function registrarAplicacao(dados) {
    let resposta;

    try {
        resposta = await apiFetch('/aplicacoes', {
            method: 'POST',
            body: JSON.stringify(dados),
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
