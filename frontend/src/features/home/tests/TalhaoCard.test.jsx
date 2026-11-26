import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import TalhaoCard from '../components/TalhaoCard.jsx';

const talhaoBase = {
    id: 1,
    codigo: 'A1',
    cultura: 'Soja',
    hectares: 10,
    estado: 'LIVRE',
    dataLiberacao: null,
};

describe('TalhaoCard - US17', () => {
    it('exibe o código e a cultura do talhão', () => {
        render(<TalhaoCard talhao={talhaoBase} />);

        expect(
            screen.getByRole('heading', { name: 'Talhão A1' })
        ).toBeInTheDocument();

        expect(
            screen.getByText('Soja')
        ).toBeInTheDocument();
    });

    it('exibe talhão livre com a classe visual correta', () => {
        render(<TalhaoCard talhao={talhaoBase} />);

        expect(
            screen.getByText('Livre')
        ).toBeInTheDocument();

        expect(
            screen.getByRole('article')
        ).toHaveClass('painel-talhao--livre');
    });

    it('exibe talhão em quarentena com data de liberação', () => {
        const talhao = {
            ...talhaoBase,
            id: 2,
            codigo: 'A2',
            cultura: 'Milho',
            estado: 'EM_QUARENTENA',
            dataLiberacao: '2026-11-30',
        };

        render(<TalhaoCard talhao={talhao} />);

        expect(
            screen.getByText('Em quarentena')
        ).toBeInTheDocument();

        expect(
            screen.getByText('Milho')
        ).toBeInTheDocument();

        expect(
            screen.getByText('30/11/2026')
        ).toBeInTheDocument();

        expect(
            screen.getByRole('article')
        ).toHaveClass('painel-talhao--quarentena');
    });

    it('não exibe data de liberação para talhão livre', () => {
        render(
            <TalhaoCard
                talhao={{
                    ...talhaoBase,
                    dataLiberacao: '2026-11-20',
                }}
            />
        );

        expect(
            screen.queryByText(/Liberação:/i)
        ).not.toBeInTheDocument();
    });

    it('exibe como livre quando o backend retorna LIVRE', () => {
        const talhao = {
            ...talhaoBase,
            codigo: 'A3',
            estado: 'LIVRE',
            dataLiberacao: '2026-11-25',
        };

        render(<TalhaoCard talhao={talhao} />);

        expect(
            screen.getByText('Livre')
        ).toBeInTheDocument();

        expect(
            screen.getByRole('article')
        ).toHaveClass('painel-talhao--livre');
    });
});