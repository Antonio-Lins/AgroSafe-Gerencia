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

/** US-13: colheita recusada em talhão em quarentena (RN01, RN04). */
@SpringBootTest
@Transactional
class ColheitaIntegracaoTest {

    private static final String USUARIO_DEMO = "operador@agrosafe.com";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private ColheitaRepository colheitaRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private Long salvarTalhao(LocalDate liberacao) {
        Talhao talhao = new Talhao("A1", new BigDecimal("12.0"), "Soja");
        talhao.setDataLiberacao(liberacao);
        return talhaoRepository.saveAndFlush(talhao).getId();
    }

    private String corpo(Long talhaoId) {
        return "{\"talhaoId\": " + talhaoId + "}";
    }

    @Test
    @WithMockUser(username = USUARIO_DEMO, roles = "OPERADOR")
    @DisplayName("CT-13-01: talhão em quarentena recusa a colheita com 409 e nada é gravado")
    void quarentenaRecusaColheita() throws Exception {
        Long id = salvarTalhao(LocalDate.now().plusDays(5));

        mockMvc.perform(post("/colheitas").contentType(APPLICATION_JSON).content(corpo(id)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("quarentena")));

        assertThat(colheitaRepository.count()).isZero();
    }

    @Test
    @WithMockUser(username = USUARIO_DEMO, roles = "OPERADOR")
    @DisplayName("CT-13-02: talhão que libera hoje aceita a colheita (RN04)")
    void liberaHojeAceitaColheita() throws Exception {
        Long id = salvarTalhao(LocalDate.now());

        mockMvc.perform(post("/colheitas").contentType(APPLICATION_JSON).content(corpo(id)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataColheita").value(LocalDate.now().toString()));

        assertThat(colheitaRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = USUARIO_DEMO, roles = "OPERADOR")
    @DisplayName("CT-13-03: talhão sem aplicações (livre) aceita a colheita")
    void talhaoLivreAceitaColheita() throws Exception {
        Long id = salvarTalhao(null);

        mockMvc.perform(post("/colheitas").contentType(APPLICATION_JSON).content(corpo(id)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("CT-13-04: sem login a colheita é recusada com 401")
    void semLoginRecusa() throws Exception {
        mockMvc.perform(post("/colheitas").contentType(APPLICATION_JSON).content(corpo(1L)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USUARIO_DEMO, roles = "OPERADOR")
    @DisplayName("CT-13-05: corpo sem talhaoId é rejeitado com 400")
    void semTalhaoIdRecusa() throws Exception {
        mockMvc.perform(post("/colheitas").contentType(APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}
