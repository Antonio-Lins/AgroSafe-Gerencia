import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';

describe('Ambiente de testes do AgroSafe', () => {
    it('deve renderizar um componente React', () => {
        render(<h1>AgroSafe</h1>);

        expect(
            screen.getByRole('heading', { name: 'AgroSafe' })
        ).toBeInTheDocument();
    });
});