import {
    describe,
    it,
    expect,
    vi,
    beforeEach,
} from 'vitest';

import {
    apiFetch,
} from '../../../shared/services/api.js';

import {
    registrarColheita,
} from '../services/colheitaService.js';

vi.mock(
    '../../../shared/services/api.js',
    () => ({
        apiFetch: vi.fn(),
    })
);

const colheita = {
    talhaoId: 1,
    dataColheita: '2026-11-26',
    quantidade: 500,
};

describe('colheitaService - UI03', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    it('envia os dados ao endpoint de colheitas', async () => {
        apiFetch.mockResolvedValue({
            ok: true,
            status: 201,
            json: async () => ({
                id: 10,
                ...colheita,
            }),
        });

        const resultado =
            await registrarColheita(colheita);

        expect(apiFetch).toHaveBeenCalledWith(
            '/colheitas',
            {
                method: 'POST',
                body: JSON.stringify(colheita),
            }
        );

        expect(resultado.ok).toBe(true);
        expect(resultado.dados.id).toBe(10);
    });

    it('retorna a mensagem de quarentena do servidor', async () => {
        apiFetch.mockResolvedValue({
            ok: false,
            status: 409,
            json: async () => ({
                detail:
                    'Talhão em quarentena. Liberação em 01/12/2026.',
            }),
        });

        const resultado =
            await registrarColheita(colheita);

        expect(resultado.ok).toBe(false);
        expect(resultado.status).toBe(409);

        expect(resultado.mensagem).toBe(
            'Talhão em quarentena. Liberação em 01/12/2026.'
        );
    });

    it('trata falha de conexão com o servidor', async () => {
        apiFetch.mockRejectedValue(
            new Error('Falha de conexão')
        );

        const resultado =
            await registrarColheita(colheita);

        expect(resultado.ok).toBe(false);
        expect(resultado.status).toBe(0);

        expect(resultado.mensagem).toContain(
            'Não foi possível conectar'
        );
    });

    it('retorna os erros de validação dos campos', async () => {
        apiFetch.mockResolvedValue({
            ok: false,
            status: 400,
            json: async () => ({
                detail: 'Dados inválidos.',
                campos: {
                    quantidade:
                        'A quantidade deve ser maior que zero',
                },
            }),
        });

        const resultado =
            await registrarColheita(colheita);

        expect(resultado.ok).toBe(false);
        expect(resultado.status).toBe(400);

        expect(resultado.campos.quantidade).toBe(
            'A quantidade deve ser maior que zero'
        );
    });
});