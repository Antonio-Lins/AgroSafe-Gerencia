import {
    render,
    screen,
    fireEvent,
    waitFor,
} from '@testing-library/react';

import userEvent from '@testing-library/user-event';

import {
    MemoryRouter,
    Routes,
    Route,
} from 'react-router-dom';

import {
    describe,
    it,
    expect,
    vi,
    beforeEach,
} from 'vitest';

import RegistrarColheita
    from '../pages/RegistrarColheita.jsx';

import {
    listarTalhoes,
} from '../../talhoes/services/talhaoService.js';

import {
    registrarColheita,
} from '../services/colheitaService.js';

vi.mock(
    '../../talhoes/services/talhaoService.js',
    () => ({
        listarTalhoes: vi.fn(),
    })
);

vi.mock(
    '../services/colheitaService.js',
    () => ({
        registrarColheita: vi.fn(),
    })
);

const talhoesMock = [
    {
        id: 1,
        codigo: 'A1',
        cultura: 'Soja',
        estado: 'LIVRE',
        dataLiberacao: null,
    },
    {
        id: 2,
        codigo: 'A2',
        cultura: 'Milho',
        estado: 'EM_QUARENTENA',
        dataLiberacao: '2026-12-01',
    },
];

function renderizarTela(
    rota = '/colheitas/nova?talhaoId=1'
) {
    return render(
        <MemoryRouter initialEntries={[rota]}>
            <Routes>
                <Route
                    path="/colheitas/nova"
                    element={<RegistrarColheita />}
                />

                <Route
                    path="/"
                    element={<h1>Painel de talhões</h1>}
                />
            </Routes>
        </MemoryRouter>
    );
}

async function preencherFormulario(
    quantidade = '500'
) {
    await screen.findByLabelText('Talhão');

    fireEvent.change(
        screen.getByLabelText('Data da colheita'),
        {
            target: {
                value: '2026-11-26',
            },
        }
    );

    fireEvent.change(
        screen.getByLabelText('Quantidade'),
        {
            target: {
                value: quantidade,
            },
        }
    );
}

async function confirmarRegistro() {
    const user = userEvent.setup();

    await user.click(
        screen.getByRole('button', {
            name: 'Registrar colheita',
        })
    );
}

describe('RegistrarColheita - UI03', () => {
    beforeEach(() => {
        vi.resetAllMocks();

        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: talhoesMock,
        });
    });

    it('seleciona automaticamente o talhão recebido pela rota', async () => {
        renderizarTela();

        expect(
            await screen.findByLabelText('Talhão')
        ).toHaveValue('1');
    });

    it('registra uma colheita com dados válidos', async () => {
        registrarColheita.mockResolvedValue({
            ok: true,
            dados: {
                id: 10,
            },
        });

        renderizarTela();

        await preencherFormulario();
        await confirmarRegistro();

        expect(
            await screen.findByText(
                'Colheita registrada com sucesso!'
            )
        ).toBeInTheDocument();

        expect(
            registrarColheita
        ).toHaveBeenCalledWith({
            talhaoId: 1,
            dataColheita: '2026-11-26',
            quantidade: 500,
        });
    });

    it('exibe a mensagem do servidor para talhão em quarentena', async () => {
        registrarColheita.mockResolvedValue({
            ok: false,
            status: 409,
            mensagem:
                'Talhão em quarentena. Liberação em 01/12/2026.',
            campos: {},
        });

        renderizarTela(
            '/colheitas/nova?talhaoId=2'
        );

        await preencherFormulario();
        await confirmarRegistro();

        expect(
            await screen.findByRole('alert')
        ).toHaveTextContent(
            'Talhão em quarentena. Liberação em 01/12/2026.'
        );

        expect(
            screen.queryByText(
                'Colheita registrada com sucesso!'
            )
        ).not.toBeInTheDocument();

        expect(
            registrarColheita
        ).toHaveBeenCalledTimes(1);
    });

    it('impede o envio com data e quantidade vazias', async () => {
        renderizarTela();

        await screen.findByLabelText('Talhão');
        await confirmarRegistro();

        expect(
            screen.getByText(
                'A data da colheita é obrigatória.'
            )
        ).toBeInTheDocument();

        expect(
            screen.getByText(
                'A quantidade é obrigatória.'
            )
        ).toBeInTheDocument();

        expect(
            registrarColheita
        ).not.toHaveBeenCalled();
    });

    it.each(['0', '-10'])(
        'impede o registro com quantidade inválida: %s',
        async (quantidade) => {
            renderizarTela();

            await preencherFormulario(quantidade);
            await confirmarRegistro();

            expect(
                screen.getByText(
                    'A quantidade deve ser maior que zero.'
                )
            ).toBeInTheDocument();

            expect(
                registrarColheita
            ).not.toHaveBeenCalled();
        }
    );

    it('preserva os campos após falha de conexão', async () => {
        registrarColheita.mockResolvedValue({
            ok: false,
            status: 0,
            mensagem:
                'Não foi possível conectar ao servidor.',
            campos: {},
        });

        renderizarTela();

        await preencherFormulario();
        await confirmarRegistro();

        expect(
            await screen.findByRole('alert')
        ).toHaveTextContent(
            'Não foi possível conectar ao servidor.'
        );

        expect(
            screen.getByLabelText('Quantidade')
        ).toHaveValue(500);

        expect(
            screen.getByLabelText('Data da colheita')
        ).toHaveValue('2026-11-26');

        expect(
            screen.getByLabelText('Talhão')
        ).toHaveValue('1');

        expect(
            screen.queryByText(
                'Colheita registrada com sucesso!'
            )
        ).not.toBeInTheDocument();
    });

    it('exige a seleção de um talhão', async () => {
        renderizarTela('/colheitas/nova');

        await preencherFormulario();
        await confirmarRegistro();

        expect(
            screen.getByText('Selecione um talhão.')
        ).toBeInTheDocument();

        expect(
            registrarColheita
        ).not.toHaveBeenCalled();
    });

    it('exibe mensagem quando não existem talhões', async () => {
        listarTalhoes.mockResolvedValue({
            ok: true,
            dados: [],
        });

        renderizarTela();

        expect(
            await screen.findByText(
                'Nenhum talhão cadastrado.'
            )
        ).toBeInTheDocument();

        expect(
            screen.getByRole('button', {
                name: 'Registrar colheita',
            })
        ).toBeDisabled();
    });

    it('exibe mensagem quando falha ao carregar os talhões', async () => {
        listarTalhoes.mockResolvedValue({
            ok: false,
            mensagem: 'Falha ao carregar os talhões.',
        });

        renderizarTela();

        expect(
            await screen.findByRole('alert')
        ).toHaveTextContent(
            'Falha ao carregar os talhões.'
        );
    });

    it('permite retornar ao painel', async () => {
        const user = userEvent.setup();

        renderizarTela();

        const botao = await screen.findByRole(
            'button',
            {
                name: 'Voltar ao painel',
            }
        );

        await user.click(botao);

        expect(
            await screen.findByRole('heading', {
                name: 'Painel de talhões',
            })
        ).toBeInTheDocument();
    });

    it('desabilita o envio enquanto registra a colheita', async () => {
        registrarColheita.mockReturnValue(
            new Promise(() => {})
        );

        renderizarTela();

        await preencherFormulario();
        await confirmarRegistro();

        await waitFor(() => {
            expect(
                screen.getByRole('button', {
                    name: 'Registrando...',
                })
            ).toBeDisabled();
        });

        expect(
            registrarColheita
        ).toHaveBeenCalledTimes(1);
    });
});