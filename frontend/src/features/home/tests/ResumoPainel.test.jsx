import { render, screen, within } from '@testing-library/react';
import { describe, it, expect } from 'vitest';

import ResumoPainel from '../components/ResumoPainel.jsx';

function criarTalhoes(livres, quarentena) {
    return [
        ...Array.from({ length: livres }, (_, index) => ({
            id: index + 1,
            estado: 'LIVRE',
        })),

        ...Array.from({ length: quarentena }, (_, index) => ({
            id: livres + index + 1,
            estado: 'EM_QUARENTENA',
        })),
    ];
}

function obterContador(titulo) {
    return screen
        .getByRole('heading', {
            name: titulo,
            exact: true,
        })
        .closest('article');
}

describe('ResumoPainel - US18', () => {
    it('exibe 10 talhões, 7 livres e 3 em quarentena', () => {
        render(
            <ResumoPainel talhoes={criarTalhoes(7, 3)} />
        );

        const total = obterContador('Total de talhões');
        const livres = obterContador('Livres');
        const quarentena = obterContador('Em quarentena');

        expect(
            within(total).getByText('10')
        ).toBeInTheDocument();

        expect(
            within(livres).getByText('7')
        ).toBeInTheDocument();

        expect(
            within(livres).getByText('70%')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('3')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('30%')
        ).toBeInTheDocument();
    });

    it('exibe os contadores zerados quando não existem talhões', () => {
        render(<ResumoPainel talhoes={[]} />);

        const total = obterContador('Total de talhões');
        const livres = obterContador('Livres');
        const quarentena = obterContador('Em quarentena');

        expect(
            within(total).getByText('0')
        ).toBeInTheDocument();

        expect(
            within(livres).getByText('0')
        ).toBeInTheDocument();

        expect(
            within(livres).getByText('0%')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('0')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('0%')
        ).toBeInTheDocument();

        expect(
            screen.queryByText(/NaN|Infinity/)
        ).not.toBeInTheDocument();
    });

    it('exibe 100% quando todos os talhões estão livres', () => {
        render(
            <ResumoPainel talhoes={criarTalhoes(5, 0)} />
        );

        const livres = obterContador('Livres');
        const quarentena = obterContador('Em quarentena');

        expect(
            within(livres).getByText('5')
        ).toBeInTheDocument();

        expect(
            within(livres).getByText('100%')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('0')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('0%')
        ).toBeInTheDocument();
    });

    it('exibe 100% quando todos os talhões estão em quarentena', () => {
        render(
            <ResumoPainel talhoes={criarTalhoes(0, 5)} />
        );

        const livres = obterContador('Livres');
        const quarentena = obterContador('Em quarentena');

        expect(
            within(livres).getByText('0%')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('5')
        ).toBeInTheDocument();

        expect(
            within(quarentena).getByText('100%')
        ).toBeInTheDocument();
    });

    it('arredonda os percentuais corretamente', () => {
        render(
            <ResumoPainel talhoes={criarTalhoes(2, 1)} />
        );

        expect(
            screen.getByText('67%')
        ).toBeInTheDocument();

        expect(
            screen.getByText('33%')
        ).toBeInTheDocument();
    });

    it('atualiza os contadores quando um talhão muda de estado', () => {
        const { rerender } = render(
            <ResumoPainel talhoes={criarTalhoes(7, 3)} />
        );

        expect(
            screen.getByText('70%')
        ).toBeInTheDocument();

        expect(
            screen.getByText('30%')
        ).toBeInTheDocument();

        rerender(
            <ResumoPainel talhoes={criarTalhoes(6, 4)} />
        );

        expect(
            screen.getByText('60%')
        ).toBeInTheDocument();

        expect(
            screen.getByText('40%')
        ).toBeInTheDocument();

        expect(
            screen.queryByText('70%')
        ).not.toBeInTheDocument();

        expect(
            screen.queryByText('30%')
        ).not.toBeInTheDocument();
    });
});