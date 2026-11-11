package br.com.agrosafe.backend.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Testes da regra RN03 (maior data de liberação) e do estado calculado (RN01). */
class TalhaoTest {

    private Talhao novoTalhao() {
        return new Talhao("A1", BigDecimal.TEN, "Soja");
    }

    @Test
    void atualizarLiberacaoSeMaior_semDataAnterior_defineAData() {
        Talhao talhao = novoTalhao();

        talhao.atualizarLiberacaoSeMaior(LocalDate.of(2026, 10, 11));

        assertThat(talhao.getDataLiberacao()).isEqualTo(LocalDate.of(2026, 10, 11));
    }

    @Test
    void atualizarLiberacaoSeMaior_comDataMaior_substituiAAtual() {
        Talhao talhao = novoTalhao();
        talhao.setDataLiberacao(LocalDate.of(2026, 10, 11));

        talhao.atualizarLiberacaoSeMaior(LocalDate.of(2026, 10, 20));

        assertThat(talhao.getDataLiberacao()).isEqualTo(LocalDate.of(2026, 10, 20));
    }

    @Test
    void atualizarLiberacaoSeMaior_comDataMenor_naoDiminui() {
        Talhao talhao = novoTalhao();
        talhao.setDataLiberacao(LocalDate.of(2026, 10, 20));

        talhao.atualizarLiberacaoSeMaior(LocalDate.of(2026, 10, 11));

        assertThat(talhao.getDataLiberacao()).isEqualTo(LocalDate.of(2026, 10, 20));
    }

    @Test
    void atualizarLiberacaoSeMaior_comDataIgual_mantemAData() {
        Talhao talhao = novoTalhao();
        talhao.setDataLiberacao(LocalDate.of(2026, 10, 20));

        talhao.atualizarLiberacaoSeMaior(LocalDate.of(2026, 10, 20));

        assertThat(talhao.getDataLiberacao()).isEqualTo(LocalDate.of(2026, 10, 20));
    }

    @Test
    void estadoEm_ficaEmQuarentenaAteODiaDaLiberacao() {
        Talhao talhao = novoTalhao();
        talhao.atualizarLiberacaoSeMaior(LocalDate.of(2026, 10, 11));

        assertThat(talhao.estadoEm(LocalDate.of(2026, 10, 10))).isEqualTo(EstadoTalhao.EM_QUARENTENA);
        assertThat(talhao.estadoEm(LocalDate.of(2026, 10, 11))).isEqualTo(EstadoTalhao.LIVRE);
        assertThat(talhao.estadoEm(LocalDate.of(2026, 10, 12))).isEqualTo(EstadoTalhao.LIVRE);
    }

    @Test
    void estadoEm_semAplicacoes_estaLivre() {
        assertThat(novoTalhao().estadoEm(LocalDate.of(2026, 10, 1))).isEqualTo(EstadoTalhao.LIVRE);
    }
}