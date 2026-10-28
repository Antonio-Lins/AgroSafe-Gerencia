package br.com.agrosafe.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.agrosafe.backend.model.Defensivo;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.repository.DefensivoRepository;
import br.com.agrosafe.backend.repository.TalhaoRepository;
import java.math.BigDecimal;
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

/**
 * US-03 (Restringir acesso por perfil): só o Gestor cadastra, edita e remove
 * talhões e defensivos (RF01, RNF03). O Operador recebe 403 e NADA é alterado no banco.
 *
 * Os testes do Operador falham enquanto o SecurityConfig não tiver @EnableMethodSecurity,
 * porque sem ele o @PreAuthorize dos controllers é ignorado.
 *
 * Os IDs CT-03-xx correspondem ao plano em docs/plano-de-testes-us-03.md.
 */
@SpringBootTest
@Transactional
class AcessoPorPerfilIntegracaoTest {

    private static final String TALHAO_JSON =
            """
            {"codigo": "A1", "hectares": 12.5, "cultura": "Soja"}
            """;

    private static final String TALHAO_ALTERADO_JSON =
            """
            {"codigo": "Z9", "hectares": 30, "cultura": "Milho"}
            """;

    private static final String DEFENSIVO_JSON =
            """
            {"nomeComercial": "Produto A", "principioAtivo": "Glifosato", "diasCarencia": 15}
            """;

    private static final String DEFENSIVO_ALTERADO_JSON =
            """
            {"nomeComercial": "Produto Z", "principioAtivo": "Atrazina", "diasCarencia": 30}
            """;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private DefensivoRepository defensivoRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Monta o MockMvc com a cadeia de segurança de verdade (SecurityConfig).
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // ------------------------------------------------------------------
    // Talhões: Operador (deve ser barrado)
    // ------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-03-01: Operador não cria talhão (403) e nada é gravado")
    void operadorNaoCriaTalhao() throws Exception {
        mockMvc.perform(post("/talhoes").contentType(APPLICATION_JSON).content(TALHAO_JSON))
                .andExpect(status().isForbidden());

        assertThat(talhaoRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-03-02: Operador não edita talhão (403) e os dados não mudam")
    void operadorNaoEditaTalhao() throws Exception {
        Talhao talhao = salvarTalhao("A1", "Soja");

        mockMvc.perform(put("/talhoes/" + talhao.getId())
                        .contentType(APPLICATION_JSON).content(TALHAO_ALTERADO_JSON))
                .andExpect(status().isForbidden());

        Talhao depois = talhaoRepository.findById(talhao.getId()).orElseThrow();
        assertThat(depois.getCodigo()).isEqualTo("A1");
        assertThat(depois.getCultura()).isEqualTo("Soja");
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-03-03: Operador não remove talhão (403) e o registro continua existindo")
    void operadorNaoRemoveTalhao() throws Exception {
        Talhao talhao = salvarTalhao("A1", "Soja");

        mockMvc.perform(delete("/talhoes/" + talhao.getId()))
                .andExpect(status().isForbidden());

        assertThat(talhaoRepository.existsById(talhao.getId())).isTrue();
    }

    // ------------------------------------------------------------------
    // Talhões: Gestor (deve passar)
    // ------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-03-04: Gestor cria talhão (201)")
    void gestorCriaTalhao() throws Exception {
        mockMvc.perform(post("/talhoes").contentType(APPLICATION_JSON).content(TALHAO_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("A1"));

        assertThat(talhaoRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-03-05: Gestor edita talhão (200)")
    void gestorEditaTalhao() throws Exception {
        Talhao talhao = salvarTalhao("A1", "Soja");

        mockMvc.perform(put("/talhoes/" + talhao.getId())
                        .contentType(APPLICATION_JSON).content(TALHAO_ALTERADO_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("Z9"))
                .andExpect(jsonPath("$.cultura").value("Milho"));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-03-06: Gestor remove talhão sem aplicações (204)")
    void gestorRemoveTalhao() throws Exception {
        Talhao talhao = salvarTalhao("A1", "Soja");

        mockMvc.perform(delete("/talhoes/" + talhao.getId()))
                .andExpect(status().isNoContent());

        assertThat(talhaoRepository.existsById(talhao.getId())).isFalse();
    }

    // ------------------------------------------------------------------
    // Defensivos: Operador (deve ser barrado)
    // ------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-03-07: Operador não cria defensivo (403) e nada é gravado")
    void operadorNaoCriaDefensivo() throws Exception {
        mockMvc.perform(post("/defensivos").contentType(APPLICATION_JSON).content(DEFENSIVO_JSON))
                .andExpect(status().isForbidden());

        assertThat(defensivoRepository.count()).isZero();
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-03-08: Operador não edita defensivo (403) e os dados não mudam")
    void operadorNaoEditaDefensivo() throws Exception {
        Defensivo defensivo = salvarDefensivo("Produto A", "Glifosato", 15);

        mockMvc.perform(put("/defensivos/" + defensivo.getId())
                        .contentType(APPLICATION_JSON).content(DEFENSIVO_ALTERADO_JSON))
                .andExpect(status().isForbidden());

        Defensivo depois = defensivoRepository.findById(defensivo.getId()).orElseThrow();
        assertThat(depois.getNomeComercial()).isEqualTo("Produto A");
        assertThat(depois.getDiasCarencia()).isEqualTo(15);
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-03-09: Operador não remove defensivo (403) e o registro continua existindo")
    void operadorNaoRemoveDefensivo() throws Exception {
        Defensivo defensivo = salvarDefensivo("Produto A", "Glifosato", 15);

        mockMvc.perform(delete("/defensivos/" + defensivo.getId()))
                .andExpect(status().isForbidden());

        assertThat(defensivoRepository.existsById(defensivo.getId())).isTrue();
    }

    // ------------------------------------------------------------------
    // Defensivos: Gestor (deve passar)
    // ------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-03-10: Gestor cria defensivo (201)")
    void gestorCriaDefensivo() throws Exception {
        mockMvc.perform(post("/defensivos").contentType(APPLICATION_JSON).content(DEFENSIVO_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeComercial").value("Produto A"));

        assertThat(defensivoRepository.count()).isEqualTo(1);
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-03-11: Gestor edita defensivo (200)")
    void gestorEditaDefensivo() throws Exception {
        Defensivo defensivo = salvarDefensivo("Produto A", "Glifosato", 15);

        mockMvc.perform(put("/defensivos/" + defensivo.getId())
                        .contentType(APPLICATION_JSON).content(DEFENSIVO_ALTERADO_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeComercial").value("Produto Z"))
                .andExpect(jsonPath("$.diasCarencia").value(30));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-03-12: Gestor remove defensivo sem aplicações (204)")
    void gestorRemoveDefensivo() throws Exception {
        Defensivo defensivo = salvarDefensivo("Produto A", "Glifosato", 15);

        mockMvc.perform(delete("/defensivos/" + defensivo.getId()))
                .andExpect(status().isNoContent());

        assertThat(defensivoRepository.existsById(defensivo.getId())).isFalse();
    }

    // ------------------------------------------------------------------
    // Sem login: 401 (e não 403)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("CT-03-13: criar talhão sem login devolve 401 e nada é gravado")
    void criarTalhaoSemLoginDevolve401() throws Exception {
        mockMvc.perform(post("/talhoes").contentType(APPLICATION_JSON).content(TALHAO_JSON))
                .andExpect(status().isUnauthorized());

        assertThat(talhaoRepository.count()).isZero();
    }

    @Test
    @DisplayName("CT-03-14: remover defensivo sem login devolve 401 e o registro continua existindo")
    void removerDefensivoSemLoginDevolve401() throws Exception {
        Defensivo defensivo = salvarDefensivo("Produto A", "Glifosato", 15);

        mockMvc.perform(delete("/defensivos/" + defensivo.getId()))
                .andExpect(status().isUnauthorized());

        assertThat(defensivoRepository.existsById(defensivo.getId())).isTrue();
    }

    // ------------------------------------------------------------------
    // Apoio
    // ------------------------------------------------------------------

    private Talhao salvarTalhao(String codigo, String cultura) {
        return talhaoRepository.save(new Talhao(codigo, new BigDecimal("12.00"), cultura));
    }

    private Defensivo salvarDefensivo(String nome, String principioAtivo, int diasCarencia) {
        return defensivoRepository.save(new Defensivo(nome, principioAtivo, diasCarencia));
    }
}
