
package br.com.agrosafe.backend.aplicacao;

import br.com.agrosafe.backend.defensivo.DefensivoRepository;
import br.com.agrosafe.backend.shared.exception.BusinessException;
import br.com.agrosafe.backend.shared.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.talhao.Talhao;
import br.com.agrosafe.backend.talhao.TalhaoRepository;
import br.com.agrosafe.backend.usuario.Perfil;
import br.com.agrosafe.backend.usuario.Usuario;
import br.com.agrosafe.backend.usuario.UsuarioRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AplicacaoCancelamentoServiceTest {

    private static final String EMAIL = "gestor@agrosafe.com";

    private final LocalDate DATA_A = LocalDate.of(2026, 10, 11);
    private final LocalDate DATA_B = LocalDate.of(2026, 10, 20);

    @Mock
    private AplicacaoRepository aplicacaoRepository;

    @Mock
    private TalhaoRepository talhaoRepository;

    @Mock
    private DefensivoRepository defensivoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Usuario gestor;

    private AplicacaoService service;
    private Talhao talhao;

    @BeforeEach
    void preparar() {
        Clock clock = Clock.fixed(
                LocalDate.of(2026, 11, 24)
                        .atStartOfDay(ZoneId.of("America/Fortaleza"))
                        .toInstant(),
                ZoneId.of("America/Fortaleza")
        );

        service = new AplicacaoService(
                aplicacaoRepository,
                talhaoRepository,
                defensivoRepository,
                usuarioRepository,
                clock
        );

        talhao = new Talhao("A1", BigDecimal.TEN, "Soja");
        ReflectionTestUtils.setField(talhao, "id", 1L);
    }

    private void configurarGestor() {
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(gestor));

        when(gestor.getPerfil()).thenReturn(Perfil.GESTOR);
    }

    private Aplicacao criarAplicacao(Long id, LocalDate liberacao) {
        Aplicacao aplicacao = new Aplicacao(
                talhao,
                null,
                gestor,
                liberacao.minusDays(10),
                liberacao
        );

        ReflectionTestUtils.setField(aplicacao, "id", id);
        return aplicacao;
    }

    @Test
    void cancelarAplicacaoMaisLonga_recalculaLiberacao() {
        configurarGestor();

        talhao.setDataLiberacao(DATA_B);
        Aplicacao aplicacaoB = criarAplicacao(2L, DATA_B);

        when(aplicacaoRepository.findById(2L))
                .thenReturn(Optional.of(aplicacaoB));

        when(aplicacaoRepository.buscarMaiorDataLiberacaoAtiva(1L))
                .thenReturn(DATA_A);

        var resposta = service.cancelar(2L, "Registro incorreto", EMAIL);

        assertThat(aplicacaoB.isCancelada()).isTrue();
        assertThat(aplicacaoB.getMotivoCancelamento())
                .isEqualTo("Registro incorreto");
        assertThat(aplicacaoB.getCanceladaPor()).isSameAs(gestor);
        assertThat(aplicacaoB.getCanceladaEm()).isNotNull();

        assertThat(talhao.getDataLiberacao()).isEqualTo(DATA_A);
        assertThat(resposta.dataLiberacaoTalhao()).isEqualTo(DATA_A);

        verify(aplicacaoRepository).saveAndFlush(aplicacaoB);
        verify(talhaoRepository).save(talhao);
    }

    @Test
    void cancelarUnicaAplicacao_removeDataLiberacao() {
        configurarGestor();

        talhao.setDataLiberacao(DATA_B);
        Aplicacao aplicacao = criarAplicacao(2L, DATA_B);

        when(aplicacaoRepository.findById(2L))
                .thenReturn(Optional.of(aplicacao));

        when(aplicacaoRepository.buscarMaiorDataLiberacaoAtiva(1L))
                .thenReturn(null);

        var resposta = service.cancelar(2L, "Aplicação indevida", EMAIL);

        assertThat(aplicacao.isCancelada()).isTrue();
        assertThat(talhao.getDataLiberacao()).isNull();
        assertThat(resposta.dataLiberacaoTalhao()).isNull();
    }

    @Test
    void cancelarAplicacaoQueNaoEraMaior_mantemLiberacao() {
        configurarGestor();

        talhao.setDataLiberacao(DATA_B);
        Aplicacao aplicacaoA = criarAplicacao(1L, DATA_A);

        when(aplicacaoRepository.findById(1L))
                .thenReturn(Optional.of(aplicacaoA));

        when(aplicacaoRepository.buscarMaiorDataLiberacaoAtiva(1L))
                .thenReturn(DATA_B);

        service.cancelar(1L, "Registro duplicado", EMAIL);

        assertThat(aplicacaoA.isCancelada()).isTrue();
        assertThat(talhao.getDataLiberacao()).isEqualTo(DATA_B);
    }

    @Test
    void cancelarSemMotivo_recusa() {
        assertThatThrownBy(() -> service.cancelar(1L, "   ", EMAIL))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("motivo");

        verify(aplicacaoRepository, never()).saveAndFlush(any());
    }

    @Test
    void cancelarAplicacaoJaCancelada_recusa() {
        configurarGestor();

        Aplicacao aplicacao = criarAplicacao(1L, DATA_A);
        aplicacao.cancelar(
                "Cancelamento anterior",
                gestor,
                java.time.LocalDateTime.now()
        );

        when(aplicacaoRepository.findById(1L))
                .thenReturn(Optional.of(aplicacao));

        assertThatThrownBy(() ->
                service.cancelar(1L, "Novo motivo", EMAIL)
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        ex -> assertThat(ex.getStatus())
                                .isEqualTo(HttpStatus.CONFLICT)
                );

        verify(aplicacaoRepository, never()).saveAndFlush(any());
    }

    @Test
    void cancelarAplicacaoInexistente_retornaNaoEncontrado() {
        configurarGestor();

        when(aplicacaoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.cancelar(99L, "Registro incorreto", EMAIL)
        )
                .isInstanceOf(ResourceNotFoundException.class);

        verify(aplicacaoRepository, never()).saveAndFlush(any());
    }

    @Test
    void operadorNaoPodeCancelar() {
        when(usuarioRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(gestor));

        when(gestor.getPerfil()).thenReturn(Perfil.OPERADOR);

        assertThatThrownBy(() ->
                service.cancelar(1L, "Registro incorreto", EMAIL)
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        ex -> assertThat(ex.getStatus())
                                .isEqualTo(HttpStatus.FORBIDDEN)
                );

        verify(aplicacaoRepository, never()).saveAndFlush(any());
    }
}

