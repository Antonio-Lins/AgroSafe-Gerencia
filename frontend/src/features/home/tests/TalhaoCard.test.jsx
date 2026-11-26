import {
    render,
    screen,
} from '@testing-library/react';

import userEvent from '@testing-library/user-event';

import {
    MemoryRouter,
    Routes,
    Route,
    useSearchParams,
} from 'react-router-dom';

import {
    describe,
    it,
    expect,
} from 'vitest';

import TalhaoCard from '../components/TalhaoCard.jsx';

const talhaoBase = {
    id: 1,
    codigo: 'A1',
    cultura: 'Soja',
    hectares: 10,
    estado: 'LIVRE',
    dataLiberacao: null,
};

const talhaoQuarentena = {
    ...talhaoBase,
    id: 2,
    codigo: 'A2',
    cultura: 'Milho',
    estado: 'EM_QUARENTENA',
    dataLiberacao: '2026-12-01',
};

function renderizarCard(talhao = talhaoBase) {
    return render(
        <MemoryRouter>
            <TalhaoCard talhao={talhao} />
        </MemoryRouter>
    );
}

function DestinoTeste({ titulo }) {
    const [searchParams] = useSearchParams();

    return (
        <div>
            <h1>{titulo}</h1>

            <p>
                Talhão selecionado: {searchParams.get('talhaoId')}
            </p>
        </div>
    );
}

function renderizarComRotas(talhao = talhaoBase) {
    return render(
        <MemoryRouter initialEntries={['/']}>
            <Routes>
                <Route
                    path="/"
                    element={<TalhaoCard talhao={talhao} />}
                />

                <Route
                    path="/aplicacoes/nova"
                    element={
                        <DestinoTeste titulo="Tela de aplicação" />
                    }
                />

                <Route
                    path="/colheitas/nova"
                    element={
                        <DestinoTeste titulo="Tela de colheita" />
                    }
                />
            </Routes>
        </MemoryRouter>
    );
}

// ==========================================
// US17 - Estados dos talhões
// ==========================================

describe('TalhaoCard - US17', () => {
    it('exibe o código e a cultura do talhão', () => {
        renderizarCard();

        expect(
            screen.getByRole('heading', {
                name: 'Talhão A1',
            })
        ).toBeInTheDocument();

        expect(
            screen.getByText('Soja')
        ).toBeInTheDocument();
    });

    it('exibe talhão livre com a classe visual correta', () => {
        renderizarCard();

        expect(
            screen.getByText('Livre')
        ).toBeInTheDocument();

        expect(
            screen.getByRole('article')
        ).toHaveClass('painel-talhao--livre');
    });

    it('exibe talhão em quarentena com data de liberação', () => {
        renderizarCard(talhaoQuarentena);

        expect(
            screen.getByText('Em quarentena')
        ).toBeInTheDocument();

        expect(
            screen.getByText('Milho')
        ).toBeInTheDocument();

        expect(
            screen.getByText('01/12/2026')
        ).toBeInTheDocument();

        expect(
            screen.getByRole('article')
        ).toHaveClass('painel-talhao--quarentena');
    });

    it('não exibe data de liberação para talhão livre', () => {
        renderizarCard({
            ...talhaoBase,
            dataLiberacao: '2026-11-20',
        });

        expect(
            screen.queryByText(/Liberação:/i)
        ).not.toBeInTheDocument();
    });

    it('exibe como livre quando o backend retorna LIVRE', () => {
        renderizarCard({
            ...talhaoBase,
            codigo: 'A3',
            estado: 'LIVRE',
            dataLiberacao: '2026-11-26',
        });

        expect(
            screen.getByText('Livre')
        ).toBeInTheDocument();

        expect(
            screen.getByRole('article')
        ).toHaveClass('painel-talhao--livre');
    });
});

// ==========================================
// US19 - Atalhos do painel
// ==========================================

describe('TalhaoCard - US19', () => {
    it('abre o registro de aplicação com o ID do talhão', async () => {
        const user = userEvent.setup();

        renderizarComRotas();

        await user.click(
            screen.getByRole('link', {
                name: 'Registrar aplicação',
            })
        );

        expect(
            await screen.findByRole('heading', {
                name: 'Tela de aplicação',
            })
        ).toBeInTheDocument();

        expect(
            screen.getByText('Talhão selecionado: 1')
        ).toBeInTheDocument();
    });

    it('abre o registro de colheita de um talhão livre', async () => {
        const user = userEvent.setup();

        renderizarComRotas();

        await user.click(
            screen.getByRole('link', {
                name: 'Registrar colheita',
            })
        );

        expect(
            await screen.findByRole('heading', {
                name: 'Tela de colheita',
            })
        ).toBeInTheDocument();

        expect(
            screen.getByText('Talhão selecionado: 1')
        ).toBeInTheDocument();
    });

    it('desabilita o atalho de colheita em quarentena', () => {
        renderizarCard(talhaoQuarentena);

        expect(
            screen.getByRole('button', {
                name: 'Registrar colheita',
            })
        ).toBeDisabled();

        expect(
            screen.queryByRole('link', {
                name: 'Registrar colheita',
            })
        ).not.toBeInTheDocument();
    });

    it('exibe a data de liberação da colheita bloqueada', () => {
        renderizarCard(talhaoQuarentena);

        expect(
            screen.getByText(
                'Colheita bloqueada até 01/12/2026.'
            )
        ).toBeInTheDocument();
    });

    it('permite acessar o registro de aplicação em quarentena', async () => {
        const user = userEvent.setup();

        renderizarComRotas(talhaoQuarentena);

        await user.click(
            screen.getByRole('link', {
                name: 'Registrar aplicação',
            })
        );

        expect(
            await screen.findByRole('heading', {
                name: 'Tela de aplicação',
            })
        ).toBeInTheDocument();

        expect(
            screen.getByText('Talhão selecionado: 2')
        ).toBeInTheDocument();
    });
});