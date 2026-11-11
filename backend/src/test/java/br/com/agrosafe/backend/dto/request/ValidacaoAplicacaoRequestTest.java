package br.com.agrosafe.backend.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Cenário de exceção da US-09: campos obrigatórios vazios. */
class ValidacaoAplicacaoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void criarValidador() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void requestValido_naoTemViolacoes() {
        assertThat(validator.validate(new AplicacaoRequest(1L, 2L, LocalDate.of(2026, 10, 1)))).isEmpty();
    }

    @Test
    void camposObrigatoriosVazios_geramViolacoes() {
        Set<ConstraintViolation<AplicacaoRequest>> violacoes =
                validator.validate(new AplicacaoRequest(null, null, null));

        assertThat(violacoes).extracting(v -> v.getPropertyPath().toString())
                .contains("talhaoId", "defensivoId", "dataAplicacao");
    }
}
