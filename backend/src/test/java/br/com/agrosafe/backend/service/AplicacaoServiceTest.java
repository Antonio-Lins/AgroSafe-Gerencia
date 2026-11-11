package br.com.agrosafe.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.agrosafe.backend.dto.request.AplicacaoRequest;
import br.com.agrosafe.backend.dto.response.AplicacaoResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.model.Aplicacao;
import br.com.agrosafe.backend.model.Defensivo;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.AplicacaoRepository;
import br.com.agrosafe.backend.repository.DefensivoRepository;
import br.com.agrosafe.backend.repository.TalhaoRepository;
import br.com.agrosafe.backend.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AplicacaoServiceTest {

    private static final String EMAIL = "operador@fazenda.com";
    private static final LocalDate DATA = LocalDate.of(2026, 10, 1);
    private static final LocalDate HOJE = LocalDate.of(2026, 11, 11);
    private static final ZoneId FUSO = ZoneId.of("America/Fortaleza");

    @Mock
    private AplicacaoRepository aplicacaoRepository;

    @Mock
    private TalhaoRepository talhaoRepository;

    @Mock
    private DefensivoRepository defensivoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Usuario usuario;

    private AplicacaoService service;

    @BeforeEach
    void setUp() {
        service = new AplicacaoService(aplicacaoRepository, talhaoRepository,
                defensivoRepository, usuarioRepository,
                Clock.fixed(HOJE.atStartOfDay(FUSO).toInstant(), FUSO));
    }

    @Test
    void registrar_comDadosValidos_calculaALiberacaoEAssociaOUsuario() {
        Talhao talhao = new Talhao("A1", BigDecimal.TEN, "Soja");
        Defensivo produtoA = new Defensivo("Produto A", "Glifosato", 10);
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(talhao));
        when(defensivoRepository.findById(2L)).thenReturn(Optional.of(produtoA));
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
        when(aplicacaoRepository.save(any(Aplicacao.class))).thenAnswer(inv -> inv.getArgument(0));

        AplicacaoResponse resposta = service.registrar(new AplicacaoRequest(1L, 2L, DATA), EMAIL);

        assertThat(resposta.dataAplicacao()).isEqualTo(LocalDate.of(2026, 10, 1));
        assertThat(resposta.dataLiberacaoCalculada()).isEqualTo(LocalDate.of(2026, 10, 11));
        assertThat(resposta.cancelada()).isFalse();

        ArgumentCaptor<Aplicacao> captor = ArgumentCaptor.forClass(Aplicacao.class);
        verify(aplicacaoRepository).save(captor.capture());
        Aplicacao salva = captor.getValue();
        assertThat(salva.getTalhao()).isSameAs(talhao);
        assertThat(salva.getDefensivo()).isSameAs(produtoA);
        assertThat(salva.getUsuario()).isSameAs(usuario);
        assertThat(salva.getDataLiberacaoCalculada()).isEqualTo(LocalDate.of(2026, 10, 11));
    }

    @Test
    void registrar_comTalhaoInexistente_recusaENaoSalva() {
        when(talhaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(new AplicacaoRequest(99L, 2L, DATA), EMAIL))
                .isInstanceOfSatisfying(BusinessException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY))
                .hasMessageContaining("Talhão");
        verify(aplicacaoRepository, never()).save(any(Aplicacao.class));
    }

    @Test
    void registrar_comDefensivoInexistente_recusaENaoSalva() {
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(new Talhao("A1", BigDecimal.TEN, "Soja")));
        when(defensivoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(new AplicacaoRequest(1L, 99L, DATA), EMAIL))
                .isInstanceOfSatisfying(BusinessException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY))
                .hasMessageContaining("Defensivo");
        verify(aplicacaoRepository, never()).save(any(Aplicacao.class));
    }

    @Test
    void registrar_comUsuarioAutenticadoInexistente_recusaENaoSalva() {
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(new Talhao("A1", BigDecimal.TEN, "Soja")));
        when(defensivoRepository.findById(2L))
                .thenReturn(Optional.of(new Defensivo("Produto A", "Glifosato", 10)));
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(new AplicacaoRequest(1L, 2L, DATA), EMAIL))
                .isInstanceOfSatisfying(BusinessException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED));
        verify(aplicacaoRepository, never()).save(any(Aplicacao.class));
    }

    @Test
    void registrar_comDataFutura_recusaCom422ENaoSalva() {
        AplicacaoRequest futura = new AplicacaoRequest(1L, 2L, HOJE.plusDays(1));

        assertThatThrownBy(() -> service.registrar(futura, EMAIL))
                .isInstanceOfSatisfying(BusinessException.class,
                        ex -> assertThat(ex.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY))
                .hasMessageContaining("futura");
        verify(aplicacaoRepository, never()).save(any(Aplicacao.class));
    }

    @Test
    void registrar_comDataDeHoje_eAceita() {
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(new Talhao("A1", BigDecimal.TEN, "Soja")));
        when(defensivoRepository.findById(2L))
                .thenReturn(Optional.of(new Defensivo("Produto A", "Glifosato", 10)));
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
        when(aplicacaoRepository.save(any(Aplicacao.class))).thenAnswer(inv -> inv.getArgument(0));

        AplicacaoResponse resposta = service.registrar(new AplicacaoRequest(1L, 2L, HOJE), EMAIL);

        assertThat(resposta.dataAplicacao()).isEqualTo(HOJE);
    }

    @Test
    void registrar_comDataPassada_eAceita() {
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(new Talhao("A1", BigDecimal.TEN, "Soja")));
        when(defensivoRepository.findById(2L))
                .thenReturn(Optional.of(new Defensivo("Produto A", "Glifosato", 10)));
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario));
        when(aplicacaoRepository.save(any(Aplicacao.class))).thenAnswer(inv -> inv.getArgument(0));

        AplicacaoResponse resposta = service.registrar(new AplicacaoRequest(1L, 2L, HOJE.minusDays(1)), EMAIL);

        assertThat(resposta.dataAplicacao()).isEqualTo(HOJE.minusDays(1));
    }
}