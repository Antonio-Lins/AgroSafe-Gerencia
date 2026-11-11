package br.com.agrosafe.backend.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Testes do cálculo da data de liberação (RN02, driver D2), incluindo os limites de calendário. */
class DefensivoTest {

    @Test
    void calcularDataLiberacao_somaOsDiasDeCarenciaADataDaAplicacao() {
        Defensivo produtoA = new Defensivo("Produto A", "Glifosato", 10);

        assertThat(produtoA.calcularDataLiberacao(LocalDate.of(2026, 10, 1)))
                .isEqualTo(LocalDate.of(2026, 10, 11));
    }

    @Test
    void calcularDataLiberacao_comCarenciaDeQuinzeDias() {
        Defensivo produtoB = new Defensivo("Produto B", "Atrazina", 15);

        assertThat(produtoB.calcularDataLiberacao(LocalDate.of(2026, 10, 5)))
                .isEqualTo(LocalDate.of(2026, 10, 20));
    }

    @Test
    void calcularDataLiberacao_atravessandoOFimDoMes() {
        Defensivo defensivo = new Defensivo("Produto A", "Glifosato", 10);

        assertThat(defensivo.calcularDataLiberacao(LocalDate.of(2026, 10, 25)))
                .isEqualTo(LocalDate.of(2026, 11, 4));
    }

    @Test
    void calcularDataLiberacao_atravessandoOFimDoAno() {
        Defensivo defensivo = new Defensivo("Produto B", "Atrazina", 15);

        assertThat(defensivo.calcularDataLiberacao(LocalDate.of(2026, 12, 20)))
                .isEqualTo(LocalDate.of(2027, 1, 4));
    }

    @Test
    void calcularDataLiberacao_emAnoBissexto() {
        Defensivo defensivo = new Defensivo("Produto A", "Glifosato", 10);

        assertThat(defensivo.calcularDataLiberacao(LocalDate.of(2028, 2, 25)))
                .isEqualTo(LocalDate.of(2028, 3, 6));
    }

    @Test
    void calcularDataLiberacao_comCarenciaDeUmDia() {
        Defensivo defensivo = new Defensivo("Produto A", "Glifosato", 1);

        assertThat(defensivo.calcularDataLiberacao(LocalDate.of(2026, 10, 1)))
                .isEqualTo(LocalDate.of(2026, 10, 2));
    }
}