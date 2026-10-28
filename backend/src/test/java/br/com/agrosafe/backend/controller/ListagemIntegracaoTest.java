package br.com.agrosafe.backend.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.agrosafe.backend.model.Defensivo;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.repository.DefensivoRepository;
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

/**
 * US-08 (Listar talhões e defensivos): testes de integração com Spring completo,
 * banco H2 e filtro de segurança reais.
 *
 * Cada teste roda dentro de uma transação que é desfeita no final (@Transactional),
 * então os dados de um teste não vazam para o outro.
 *
 * Os IDs CT-08-xx correspondem ao plano em docs/plano-de-testes-us-08.md.
 */
@SpringBootTest
@Transactional
class ListagemIntegracaoTest {

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
    // Talhões
    // ------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-01: lista de talhões vazia devolve 200 e []")
    void listaDeTalhoesVaziaDevolveArrayVazio() throws Exception {
        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-02: talhões vêm ordenados por código e com todos os campos")
    void talhoesVemOrdenadosPorCodigo() throws Exception {
        salvarTalhao("C3", "Milho", null);
        salvarTalhao("A1", "Soja", null);
        salvarTalhao("B2", "Feijão", null);

        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].codigo").value("A1"))
                .andExpect(jsonPath("$[1].codigo").value("B2"))
                .andExpect(jsonPath("$[2].codigo").value("C3"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].hectares").value(12.0))
                .andExpect(jsonPath("$[0].cultura").value("Soja"));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-03: talhão sem data de liberação aparece como LIVRE")
    void talhaoSemDataDeLiberacaoEstaLivre() throws Exception {
        salvarTalhao("A1", "Soja", null);

        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("LIVRE"))
                .andExpect(jsonPath("$[0].dataLiberacao").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-04: talhão com data de liberação futura aparece EM_QUARENTENA")
    void talhaoComLiberacaoFuturaEstaEmQuarentena() throws Exception {
        LocalDate liberacao = LocalDate.now().plusDays(5);
        salvarTalhao("A1", "Soja", liberacao);

        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("EM_QUARENTENA"))
                .andExpect(jsonPath("$[0].dataLiberacao").value(liberacao.toString()));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-05: talhão que libera hoje aparece LIVRE (RN04: pode colher no próprio dia)")
    void talhaoQueLiberaHojeEstaLivre() throws Exception {
        salvarTalhao("A1", "Soja", LocalDate.now());

        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("LIVRE"));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-06: talhão com liberação no passado aparece LIVRE")
    void talhaoComLiberacaoPassadaEstaLivre() throws Exception {
        salvarTalhao("A1", "Soja", LocalDate.now().minusDays(1));

        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("LIVRE"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-08-07: Operador também consegue listar talhões")
    void operadorListaTalhoes() throws Exception {
        salvarTalhao("A1", "Soja", null);

        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("CT-08-08: listar talhões sem login devolve 401")
    void listarTalhoesSemLoginDevolve401() throws Exception {
        mockMvc.perform(get("/talhoes"))
                .andExpect(status().isUnauthorized());
    }

    // ------------------------------------------------------------------
    // Defensivos
    // ------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-09: lista de defensivos vazia devolve 200 e []")
    void listaDeDefensivosVaziaDevolveArrayVazio() throws Exception {
        mockMvc.perform(get("/defensivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "GESTOR")
    @DisplayName("CT-08-10: defensivos vêm ordenados por nome comercial e com todos os campos")
    void defensivosVemOrdenadosPorNomeComercial() throws Exception {
        defensivoRepository.save(new Defensivo("Zeta", "Glifosato", 15));
        defensivoRepository.save(new Defensivo("Alfa", "Tebuconazol", 10));

        mockMvc.perform(get("/defensivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomeComercial").value("Alfa"))
                .andExpect(jsonPath("$[0].principioAtivo").value("Tebuconazol"))
                .andExpect(jsonPath("$[0].diasCarencia").value(10))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].nomeComercial").value("Zeta"));
    }

    @Test
    @WithMockUser(roles = "OPERADOR")
    @DisplayName("CT-08-11: Operador também consegue listar defensivos")
    void operadorListaDefensivos() throws Exception {
        defensivoRepository.save(new Defensivo("Alfa", "Tebuconazol", 10));

        mockMvc.perform(get("/defensivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("CT-08-12: listar defensivos sem login devolve 401")
    void listarDefensivosSemLoginDevolve401() throws Exception {
        mockMvc.perform(get("/defensivos"))
                .andExpect(status().isUnauthorized());
    }

    // ------------------------------------------------------------------
    // Apoio
    // ------------------------------------------------------------------

    private Talhao salvarTalhao(String codigo, String cultura, LocalDate dataLiberacao) {
        Talhao talhao = new Talhao(codigo, new BigDecimal("12.00"), cultura);
        talhao.setDataLiberacao(dataLiberacao);
        return talhaoRepository.save(talhao);
    }
}
