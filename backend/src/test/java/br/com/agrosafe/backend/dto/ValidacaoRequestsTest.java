package br.com.agrosafe.backend.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.agrosafe.backend.dto.request.DefensivoRequest;
import br.com.agrosafe.backend.dto.request.TalhaoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidacaoRequestsTest {

    private static Validator validator;

    @BeforeAll
    static void criarValidador() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void defensivoValido_naoTemViolacoes() {
        assertThat(validator.validate(new DefensivoRequest("Produto A", "Glifosato", 15))).isEmpty();
    }

    @Test
    void defensivoComCamposVazios_geraViolacoes() {
        Set<ConstraintViolation<DefensivoRequest>> violacoes =
                validator.validate(new DefensivoRequest(" ", "", null));

        assertThat(violacoes).extracting(v -> v.getPropertyPath().toString())
                .contains("nomeComercial", "principioAtivo", "diasCarencia");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -15})
    void carenciaZeroOuNegativa_geraViolacao(int carencia) {
        Set<ConstraintViolation<DefensivoRequest>> violacoes =
                validator.validate(new DefensivoRequest("Produto A", "Glifosato", carencia));

        assertThat(violacoes).extracting(ConstraintViolation::getMessage)
                .contains("A carência deve ser um inteiro maior que zero");
    }

    @Test
    void talhaoValido_naoTemViolacoes() {
        assertThat(validator.validate(new TalhaoRequest("A1", new BigDecimal("12"), "Soja"))).isEmpty();
    }

    @Test
    void talhaoComCamposVazios_geraViolacoes() {
        Set<ConstraintViolation<TalhaoRequest>> violacoes =
                validator.validate(new TalhaoRequest("", null, " "));

        assertThat(violacoes).extracting(v -> v.getPropertyPath().toString())
                .contains("codigo", "hectares", "cultura");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-0.5"})
    void hectaresZeroOuNegativo_geraViolacao(String hectares) {
        Set<ConstraintViolation<TalhaoRequest>> violacoes =
                validator.validate(new TalhaoRequest("A1", new BigDecimal(hectares), "Soja"));

        assertThat(violacoes).extracting(ConstraintViolation::getMessage)
                .contains("Os hectares devem ser maiores que zero");
    }
}
