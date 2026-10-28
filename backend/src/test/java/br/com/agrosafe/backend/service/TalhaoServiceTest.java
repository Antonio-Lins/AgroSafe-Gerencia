package br.com.agrosafe.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.agrosafe.backend.dto.request.TalhaoRequest;
import br.com.agrosafe.backend.dto.response.TalhaoResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.model.EstadoTalhao;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.repository.AplicacaoRepository;
import br.com.agrosafe.backend.repository.TalhaoRepository;
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

@ExtendWith(MockitoExtension.class)
public class TalhaoServiceTest {
    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    // "hoje" fixo nos testes: 20/10/2026
    private static final LocalDate HOJE = LocalDate.of(2026, 10, 20);
    private final Clock clock = Clock.fixed(HOJE.atStartOfDay(FUSO).toInstant(), FUSO);

    @Mock
    private TalhaoRepository talhaoRepository;

    @Mock
    private AplicacaoRepository aplicacaoRepository;

    private TalhaoService service;

    @BeforeEach
    void setUp() {
        service = new TalhaoService(talhaoRepository, aplicacaoRepository, clock);
    }

    @Test
    void criar_comDadosValidos_salvaTalhaoLivre() {
        when(talhaoRepository.existsByCodigo("A1")).thenReturn(false);
        when(talhaoRepository.saveAndFlush(any(Talhao.class))).thenAnswer(inv -> inv.getArgument(0));

        TalhaoResponse resposta = service.criar(new TalhaoRequest(" A1 ", new BigDecimal("12"), "Soja"));

        assertThat(resposta.codigo()).isEqualTo("A1");
        assertThat(resposta.cultura()).isEqualTo("Soja");
        assertThat(resposta.estado()).isEqualTo(EstadoTalhao.LIVRE);
        verify(talhaoRepository).saveAndFlush(any(Talhao.class));
    }

    @Test
    void criar_comCodigoJaExistente_lancaConflitoENaoSalva() {
        when(talhaoRepository.existsByCodigo("A1")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(new TalhaoRequest("A1", BigDecimal.TEN, "Soja")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("A1");
        verify(talhaoRepository, never()).saveAndFlush(any(Talhao.class));
    }

    @Test
    void atualizar_comDadosValidos_alteraOsCampos() {
        Talhao existente = new Talhao("A1", new BigDecimal("12"), "Soja");
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(talhaoRepository.existsByCodigoAndIdNot("A1", 1L)).thenReturn(false);
        when(talhaoRepository.saveAndFlush(existente)).thenReturn(existente);

        TalhaoResponse resposta = service.atualizar(1L, new TalhaoRequest("A1", new BigDecimal("12"), "Milho"));

        assertThat(resposta.cultura()).isEqualTo("Milho");
    }

    @Test
    void atualizar_paraCodigoDeOutroTalhao_lancaConflitoENaoSalva() {
        when(talhaoRepository.findById(2L)).thenReturn(Optional.of(new Talhao("A2", BigDecimal.ONE, "Soja")));
        when(talhaoRepository.existsByCodigoAndIdNot("A1", 2L)).thenReturn(true);

        assertThatThrownBy(() -> service.atualizar(2L, new TalhaoRequest("A1", BigDecimal.ONE, "Soja")))
                .isInstanceOf(BusinessException.class);
        verify(talhaoRepository, never()).saveAndFlush(any(Talhao.class));
    }

    @Test
    void atualizar_talhaoInexistente_lancaNaoEncontrado() {
        when(talhaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99L, new TalhaoRequest("A1", BigDecimal.ONE, "Soja")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void remover_semAplicacoes_removeOTalhao() {
        Talhao talhao = new Talhao("A1", BigDecimal.TEN, "Soja");
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(talhao));
        when(aplicacaoRepository.existsByTalhaoId(1L)).thenReturn(false);

        service.remover(1L);

        verify(talhaoRepository).delete(talhao);
    }

    @Test
    void remover_comAplicacoes_lancaConflitoENaoRemove() {
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(new Talhao("A1", BigDecimal.TEN, "Soja")));
        when(aplicacaoRepository.existsByTalhaoId(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.remover(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("histórico");
        verify(talhaoRepository, never()).delete(any(Talhao.class));
    }

    @Test
    void buscar_talhaoInexistente_lancaNaoEncontrado() {
        when(talhaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void buscar_semDataDeLiberacao_estaLivre() {
        assertThat(estadoComLiberacao(null)).isEqualTo(EstadoTalhao.LIVRE);
    }

    @Test
    void buscar_comLiberacaoFutura_estaEmQuarentena() {
        assertThat(estadoComLiberacao(HOJE.plusDays(1))).isEqualTo(EstadoTalhao.EM_QUARENTENA);
    }

    @Test
    void buscar_comLiberacaoHoje_estaLivre() {
        assertThat(estadoComLiberacao(HOJE)).isEqualTo(EstadoTalhao.LIVRE);
    }

    private EstadoTalhao estadoComLiberacao(LocalDate dataLiberacao) {
        Talhao talhao = new Talhao("A1", BigDecimal.TEN, "Soja");
        talhao.setDataLiberacao(dataLiberacao);
        when(talhaoRepository.findById(1L)).thenReturn(Optional.of(talhao));
        return service.buscar(1L).estado();
    }
}
