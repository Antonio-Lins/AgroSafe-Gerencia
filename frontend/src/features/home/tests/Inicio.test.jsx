import {
    render,
    screen,
    within,
} from '@testing-library/react';

import userEvent from '@testing-library/user-event';

import { MemoryRouter } from 'react-router-dom';

import {
    describe,
    it,
    expect,
    vi,
    beforeEach,
} from 'vitest';

import Inicio from '../pages/Inicio.jsx';

import { useAuth } from '../../auth/AuthContext.jsx';

import {
    listarTalhoes,
} from '../../talhoes/services/talhaoService.js';

vi.mock('../../auth/AuthContext.jsx', () => ({
    useAuth: vi.fn(),
}));

vi.mock(
    '../../talhoes/services/talhaoService.js',
    () => ({
        listarTalhoes: vi.fn(),
    })
);

const talhoesMock = [
    {
        id: 1,
        codigo: 'A1',
        cultura: 'Soja',
        hectares: 10,
        estado: 'LIVRE',
        dataLiberacao: null,
    },
    {
        id: 2,
        codigo: 'A2',
        cultura: 'Milho',
        hectares: 15,
        estado: 'EM_QUARENTENA',
        dataLiberacao: '2026-11-30',
    },
];

function renderizarPainel() {
    return render(
        <MemoryRouter>
            <Inicio />
        </MemoryRouter>
    );
}

// ==========================================
// US17 - Estados dos talhões
// ==========================================

describe('Painel de talhões - US17', () => {
    beforeEach(() => {
        vi.resetAllMocks();

        useAuth.mockReturnValue({
            usuario: {
                nome: 'Gestor',
                perfil: 'GESTOR',
            },
            ehGestor: true,
        });
    });

    it('exibe mensagem enquanto carrega os talhões', () => {
        listarTalhoes.mockReturnValue(
            new Promise(() => {})
        );

        renderizarPainel();

        expect(
            screen.getByRole('status')
        ).toHaveTextContent('Carregando talhões');
    });

    it('exibe os talhões retornados pelo backend', async () => {
        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: talhoesMock,
        });

        renderizarPainel();

        const cards = await screen.findAllByRole('article', {
            name: '',
        });

        expect(cards).toHaveLength(2);

        expect(
            within(cards[0]).getByText('Livre')
        ).toBeInTheDocument();

        expect(
            within(cards[1]).getByText('Em quarentena')
        ).toBeInTheDocument();
    });

    it('exibe mensagem quando não existem talhões', async () => {
        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: [],
        });

        renderizarPainel();

        expect(
            await screen.findByText('Nenhum talhão cadastrado.')
        ).toBeInTheDocument();
    });

    it('permite que o operador visualize os talhões', async () => {
        useAuth.mockReturnValue({
            usuario: {
                nome: 'Operador',
                perfil: 'OPERADOR',
            },
            ehGestor: false,
        });

        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: talhoesMock,
        });

        renderizarPainel();

        expect(
            await screen.findAllByRole('article', {
                name: '',
            })
        ).toHaveLength(2);
    });

    it('exibe mensagem quando ocorre falha na consulta', async () => {
        listarTalhoes.mockResolvedValue({
            ok: false,
            mensagem: 'Falha de conexão.',
        });

        renderizarPainel();

        expect(
            await screen.findByRole('alert')
        ).toHaveTextContent('Falha de conexão.');

        expect(
            screen.queryByText('Nenhum talhão cadastrado.')
        ).not.toBeInTheDocument();
    });

    it('permite tentar novamente após uma falha', async () => {
        const user = userEvent.setup();

        listarTalhoes
            .mockResolvedValueOnce({
                ok: false,
                mensagem: 'Falha de conexão.',
            })
            .mockResolvedValueOnce({
                ok: true,
                dados: talhoesMock,
            });

        renderizarPainel();

        const botao = await screen.findByRole('button', {
            name: 'Tentar novamente',
        });

        await user.click(botao);

        expect(
            await screen.findAllByRole('article', {
                name: '',
            })
        ).toHaveLength(2);

        expect(listarTalhoes).toHaveBeenCalledTimes(2);
    });

    it('consulta novamente os estados ao retornar ao painel', async () => {
        listarTalhoes.mockResolvedValueOnce({
            ok: true,
            dados: [talhoesMock[1]],
        });

        const { unmount } = renderizarPainel();

        const [cardAntes] = await screen.findAllByRole('article', {
            name: '',
        });

        expect(
            within(cardAntes).getByText('Em quarentena')
        ).toBeInTheDocument();

        unmount();

        listarTalhoes.mockResolvedValueOnce({
            ok: true,
            dados: [
                {
                    ...talhoesMock[1],
                    estado: 'LIVRE',
                },
            ],
        });

        renderizarPainel();

        const [cardDepois] = await screen.findAllByRole('article', {
            name: '',
        });

        expect(
            within(cardDepois).getByText('Livre')
        ).toBeInTheDocument();

        expect(
            within(cardDepois).queryByText('Em quarentena')
        ).not.toBeInTheDocument();

        expect(listarTalhoes).toHaveBeenCalledTimes(2);
    });
});

// ==========================================
// US18 - Contadores do painel
// ==========================================

describe('Painel de talhões - US18', () => {
    beforeEach(() => {
        vi.resetAllMocks();

        useAuth.mockReturnValue({
            usuario: {
                nome: 'Gestor',
                perfil: 'GESTOR',
            },
            ehGestor: true,
        });
    });

    it('exibe os contadores para o gestor', async () => {
        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: talhoesMock,
        });

        renderizarPainel();

        const resumo = await screen.findByRole('region', {
            name: 'Resumo dos talhões',
        });

        expect(
            within(resumo).getByText('Total de talhões')
        ).toBeInTheDocument();

        expect(
            within(resumo).getAllByText('50%')
        ).toHaveLength(2);
    });

    it('não exibe os contadores para o operador', async () => {
        useAuth.mockReturnValue({
            usuario: {
                nome: 'Operador',
                perfil: 'OPERADOR',
            },
            ehGestor: false,
        });

        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: talhoesMock,
        });

        renderizarPainel();

        await screen.findByRole('heading', {
            name: 'Talhão A1',
        });

        expect(
            screen.queryByRole('region', {
                name: 'Resumo dos talhões',
            })
        ).not.toBeInTheDocument();
    });

    it('exibe percentuais zerados quando não existem talhões', async () => {
        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: [],
        });

        renderizarPainel();

        const resumo = await screen.findByRole('region', {
            name: 'Resumo dos talhões',
        });

        expect(
            within(resumo).getAllByText('0%')
        ).toHaveLength(2);

        expect(
            screen.getByText('Nenhum talhão cadastrado.')
        ).toBeInTheDocument();
    });

    it('não exibe contadores quando a consulta falha', async () => {
        listarTalhoes.mockResolvedValue({
            ok: false,
            mensagem: 'Falha de conexão.',
        });

        renderizarPainel();

        await screen.findByRole('alert');

        expect(
            screen.queryByRole('region', {
                name: 'Resumo dos talhões',
            })
        ).not.toBeInTheDocument();
    });

    it('atualiza os contadores ao retornar ao painel', async () => {
        listarTalhoes.mockResolvedValueOnce({
            ok: true,
            dados: [
                talhoesMock[0],
                {
                    ...talhoesMock[1],
                    estado: 'LIVRE',
                    dataLiberacao: null,
                },
            ],
        });

        const { unmount } = renderizarPainel();

        expect(
            await screen.findByText('100%')
        ).toBeInTheDocument();

        unmount();

        listarTalhoes.mockResolvedValueOnce({
            ok: true,
            dados: talhoesMock,
        });

        renderizarPainel();

        const resumo = await screen.findByRole('region', {
            name: 'Resumo dos talhões',
        });

        expect(
            within(resumo).getAllByText('50%')
        ).toHaveLength(2);

        expect(
            screen.queryByText('100%')
        ).not.toBeInTheDocument();

        expect(listarTalhoes).toHaveBeenCalledTimes(2);
    });
});