
package br.com.agrosafe.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.repository.ColheitaRepository;
import br.com.agrosafe.backend.repository.TalhaoRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
class ColheitaIntegracaoTest {

    private static final String USUARIO_DEMO =
            "operador@agrosafe.com";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private ColheitaRepository colheitaRepository;

    @Autowired
    private Clock clock;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private LocalDate hoje() {
        return LocalDate.now(clock);
    }

    private Long salvarTalhao(LocalDate liberacao) {
        Talhao talhao = new Talhao(
                "A1",
                new BigDecimal("12.0"),
                "Soja"
        );

        talhao.setDataLiberacao(liberacao);

        return talhaoRepository
                .saveAndFlush(talhao)
                .getId();
    }

    private String corpo(Long talhaoId) {
        return """
                {
                    "talhaoId": %d,
                    "dataColheita": "%s",
                    "quantidade": 500
                }
                """.formatted(talhaoId, hoje());
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Talhão em quarentena recusa colheita")
    void quarentenaRecusaColheita() throws Exception {

        Long id = salvarTalhao(hoje().plusDays(5));

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content(corpo(id))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value(org.hamcrest.Matchers
                                .containsString("quarentena")));

        assertThat(colheitaRepository.count()).isZero();
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Talhão liberado hoje aceita colheita")
    void liberaHojeAceitaColheita() throws Exception {

        Long id = salvarTalhao(hoje());

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content(corpo(id))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataColheita")
                        .value(hoje().toString()))
                .andExpect(jsonPath("$.quantidade")
                        .value(500));

        assertThat(colheitaRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Talhão livre aceita colheita")
    void talhaoLivreAceitaColheita() throws Exception {

        Long id = salvarTalhao(null);

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content(corpo(id))
                )
                .andExpect(status().isCreated());

        assertThat(colheitaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Sem login o acesso é recusado")
    void semLoginRecusa() throws Exception {

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content(corpo(1L))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Talhão obrigatório")
    void semTalhaoIdRecusa() throws Exception {

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Data obrigatória")
    void semDataRecusa() throws Exception {

        String json = """
                {
                    "talhaoId": 1,
                    "quantidade": 500
                }
                """;

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Quantidade obrigatória")
    void semQuantidadeRecusa() throws Exception {

        String json = """
                {
                    "talhaoId": 1,
                    "dataColheita": "2026-11-11"
                }
                """;

        mockMvc.perform(
                        post("/colheitas")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(
            username = USUARIO_DEMO,
            roles = "OPERADOR"
    )
    @DisplayName("Quantidade deve ser positiva")
    void quantidadeInvalidaRecusa() throws Exception {

        for (int quantidade : new int[]{0, -10}) {

            String json = """
                    {
                        "talhaoId": 1,
                        "dataColheita": "2026-11-11",
                        "quantidade": %d
                    }
                    """.formatted(quantidade);

            mockMvc.perform(
                            post("/colheitas")
                                    .contentType(APPLICATION_JSON)
                                    .content(json)
                    )
                    .andExpect(status().isBadRequest());
        }
    }
}
