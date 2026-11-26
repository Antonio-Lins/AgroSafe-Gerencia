package br.com.agrosafe.backend;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("migrationtest")
class FlywayMigrationIT {

    @Autowired
    private Flyway flyway;

    @Test
    void deveAplicarEValidarMigracoesNoPostgreSQL() {
        var resultado = flyway.validateWithResult();

        assertTrue(
                resultado.validationSuccessful,
                "As migracoes Flyway devem ser validas"
        );

        assertTrue(
                flyway.info().applied().length > 0,
                "O banco deve possuir migracoes aplicadas"
        );
    }
}
