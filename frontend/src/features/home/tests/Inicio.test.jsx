import {
    render,
    screen,
    within,
} from '@testing-library/react';

import userEvent from '@testing-library/user-event';

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

        render(<Inicio />);

        expect(
            screen.getByRole('status')
        ).toHaveTextContent('Carregando talhões');
    });

    it('exibe os talhões retornados pelo backend', async () => {
        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: talhoesMock,
        });

        render(<Inicio />);

        const cards = await screen.findAllByRole('article');

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

        render(<Inicio />);

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

        render(<Inicio />);

        expect(
            await screen.findAllByRole('article')
        ).toHaveLength(2);
    });

    it('exibe mensagem quando ocorre falha na consulta', async () => {
        listarTalhoes.mockResolvedValue({
            ok: false,
            mensagem: 'Falha de conexão.',
        });

        render(<Inicio />);

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

        render(<Inicio />);

        const botao = await screen.findByRole('button', {
            name: 'Tentar novamente',
        });

        await user.click(botao);

        expect(
            await screen.findAllByRole('article')
        ).toHaveLength(2);

        expect(listarTalhoes).toHaveBeenCalledTimes(2);
    });

    it('consulta novamente os estados ao retornar ao painel', async () => {
        listarTalhoes.mockResolvedValueOnce({
            ok: true,
            dados: [talhoesMock[1]],
        });

        const { unmount } = render(<Inicio />);

        expect(
            await screen.findByText('Em quarentena')
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

        render(<Inicio />);

        expect(
            await screen.findByText('Livre')
        ).toBeInTheDocument();

        expect(
            screen.queryByText('Em quarentena')
        ).not.toBeInTheDocument();
    });
});