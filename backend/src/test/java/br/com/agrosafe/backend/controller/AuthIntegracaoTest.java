package br.com.agrosafe.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.agrosafe.backend.model.Perfil;
import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.TalhaoRepository;
import br.com.agrosafe.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * US-02 (Fazer login): login por e-mail e senha, sessão, logout e usuário logado.
 * Aqui o login é REAL (sem @WithMockUser): os usuários são gravados no banco com
 * senha em hash BCrypt, como na aplicação de verdade.
 *
 * Os e-mails usam o domínio ".test" para não colidir com a carga inicial (US-04).
 *
 * Os IDs CT-02-xx correspondem ao plano em docs/plano-de-testes-us-02.md.
 */
@SpringBootTest
@Transactional
class AuthIntegracaoTest {

    private static final String EMAIL_GESTOR = "teste.gestor@agrosafe.test";
    private static final String EMAIL_OPERADOR = "teste.operador@agrosafe.test";
    private static final String SENHA = "senha-segura-123";

    private static final String TALHAO_JSON =
            """
            {"codigo": "A1", "hectares": 12.5, "cultura": "Soja"}
            """;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TalhaoRepository talhaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Monta o MockMvc com a cadeia de segurança de verdade (SecurityConfig).
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        usuarioRepository.save(new Usuario(
                "Maria Gestora", EMAIL_GESTOR, passwordEncoder.encode(SENHA), Perfil.GESTOR));
        usuarioRepository.save(new Usuario(
                "José Operador", EMAIL_OPERADOR, passwordEncoder.encode(SENHA), Perfil.OPERADOR));
    }

    // ------------------------------------------------------------------
    // Login válido
    // ------------------------------------------------------------------

    @Test
    @DisplayName("CT-02-01: Gestor faz login com credenciais válidas (200, nome e perfil, sem senha)")
    void gestorFazLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson(EMAIL_GESTOR, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Gestora"))
                .andExpect(jsonPath("$.email").value(EMAIL_GESTOR))
                .andExpect(jsonPath("$.perfil").value("GESTOR"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.senhaHash").doesNotExist());
    }

    @Test
    @DisplayName("CT-02-02: Operador faz login com credenciais válidas (200 e perfil OPERADOR)")
    void operadorFazLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson(EMAIL_OPERADOR, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.perfil").value("OPERADOR"));
    }

    // ------------------------------------------------------------------
    // Login inválido
    // ------------------------------------------------------------------

    @Test
    @DisplayName("CT-02-03: senha errada devolve 401 com mensagem genérica")
    void senhaErradaDevolve401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson(EMAIL_GESTOR, "senha-errada")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("E-mail ou senha inválidos."));
    }

    @Test
    @DisplayName("CT-02-04: e-mail inexistente devolve 401 com mensagem genérica")
    void emailInexistenteDevolve401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson("naoexiste@agrosafe.test", SENHA)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("E-mail ou senha inválidos."));
    }

    @Test
    @DisplayName("CT-02-05: a resposta é idêntica para senha errada e e-mail inexistente (não revela se o e-mail existe)")
    void respostaNaoRevelaSeOEmailExiste() throws Exception {
        String senhaErrada = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson(EMAIL_GESTOR, "senha-errada")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        String emailInexistente = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson("naoexiste@agrosafe.test", SENHA)))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        assertThat(senhaErrada).isEqualTo(emailInexistente);
    }

    @Test
    @DisplayName("CT-02-06: e-mail e senha vazios devolvem 400")
    void camposVaziosDevolve400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson("", "")))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------------
    // Acesso sem login e sessão
    // ------------------------------------------------------------------

    @Test
    @DisplayName("CT-02-07: rotas protegidas sem login devolvem 401")
    void rotasProtegidasSemLoginDevolvem401() throws Exception {
        mockMvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/talhoes")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/defensivos")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("CT-02-08: depois do login, a sessão mantém o usuário logado")
    void sessaoMantemOUsuarioLogado() throws Exception {
        MockHttpSession sessao = logar(EMAIL_GESTOR);

        mockMvc.perform(get("/api/auth/me").session(sessao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL_GESTOR))
                .andExpect(jsonPath("$.perfil").value("GESTOR"));

        mockMvc.perform(get("/talhoes").session(sessao))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CT-02-09: logout encerra a sessão (204) e o acesso volta a ser 401")
    void logoutEncerraASessao() throws Exception {
        MockHttpSession sessao = logar(EMAIL_GESTOR);

        mockMvc.perform(post("/api/auth/logout").session(sessao))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/auth/me").session(sessao))
                .andExpect(status().isUnauthorized());
    }

    // ------------------------------------------------------------------
    // Perfil (ligação com a US-03)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("CT-02-10: o Perfil vira a autoridade ROLE_GESTOR / ROLE_OPERADOR")
    void perfilViraAutoridadeDoSpringSecurity() {
        UserDetails gestor = userDetailsService.loadUserByUsername(EMAIL_GESTOR);
        UserDetails operador = userDetailsService.loadUserByUsername(EMAIL_OPERADOR);

        assertThat(gestor.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .containsExactly("ROLE_GESTOR");
        assertThat(operador.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .containsExactly("ROLE_OPERADOR");
    }

    @Test
    @DisplayName("CT-02-11: ponta a ponta: Gestor logado de verdade cria talhão (201)")
    void gestorLogadoCriaTalhao() throws Exception {
        MockHttpSession sessao = logar(EMAIL_GESTOR);

        mockMvc.perform(post("/talhoes").session(sessao)
                        .contentType(APPLICATION_JSON).content(TALHAO_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("A1"));

        assertThat(talhaoRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("CT-02-12: ponta a ponta: Operador logado de verdade não cria talhão (403)")
    void operadorLogadoNaoCriaTalhao() throws Exception {
        MockHttpSession sessao = logar(EMAIL_OPERADOR);

        mockMvc.perform(post("/talhoes").session(sessao)
                        .contentType(APPLICATION_JSON).content(TALHAO_JSON))
                .andExpect(status().isForbidden());

        assertThat(talhaoRepository.count()).isZero();
    }

    // ------------------------------------------------------------------
    // Apoio
    // ------------------------------------------------------------------

    private String loginJson(String email, String senha) {
        return "{\"email\": \"%s\", \"senha\": \"%s\"}".formatted(email, senha);
    }

    /** Faz login de verdade e devolve a sessão aberta, para usar nas próximas requisições. */
    private MockHttpSession logar(String email) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON).content(loginJson(email, SENHA)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) resultado.getRequest().getSession(false);
    }
}
