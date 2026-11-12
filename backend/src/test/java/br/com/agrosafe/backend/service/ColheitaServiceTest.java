
package br.com.agrosafe.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.agrosafe.backend.dto.request.ColheitaRequest;
import br.com.agrosafe.backend.dto.response.ColheitaResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.model.Colheita;
import br.com.agrosafe.backend.model.Talhao;
import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.ColheitaRepository;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ColheitaServiceTest {

    private static final String EMAIL = "operador@fazenda.com";
    private static final LocalDate HOJE = LocalDate.of(2026, 11, 11);
    private static final ZoneId FUSO = ZoneId.of("America/Fortaleza");

    @Mock
    private ColheitaRepository colheitaRepository;

    @Mock
    private TalhaoRepository talhaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Usuario usuario;

    private ColheitaService service;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(
                HOJE.atStartOfDay(FUSO).toInstant(),
                FUSO
        );

        service = new ColheitaService(
                colheitaRepository,
                talhaoRepository,
                usuarioRepository,
                clock
        );
    }

    private ColheitaRequest request(Long talhaoId) {
        return new ColheitaRequest(
                talhaoId,
                HOJE,
                new BigDecimal("500")
        );
    }

    private Talhao talhaoComLiberacao(LocalDate liberacao) {
        Talhao talhao = new Talhao(
                "A1",
                BigDecimal.TEN,
                "Soja"
        );

        talhao.setDataLiberacao(liberacao);

        when(talhaoRepository.findById(1L))
                .thenReturn(Optional.of(talhao));

        when(usuarioRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(usuario));

        return talhao;
    }

    private void esperaSalvarEDevolver() {
        when(colheitaRepository.save(any(Colheita.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void registrar_emQuarentena_recusaCom409ENaoSalva() {

        talhaoComLiberacao(HOJE.plusDays(1));

        assertThatThrownBy(() ->
                service.registrar(request(1L), EMAIL)
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        ex -> assertThat(ex.getStatus())
                                .isEqualTo(HttpStatus.CONFLICT)
                )
                .hasMessageContaining("quarentena")
                .hasMessageContaining("12/11/2026");

        verify(colheitaRepository, never())
                .save(any(Colheita.class));
    }

    @Test
    void registrar_liberandoHoje_eAceitaPoisRN04PermiteColherNoProprioDia() {

        talhaoComLiberacao(HOJE);
        esperaSalvarEDevolver();

        when(usuario.getId()).thenReturn(7L);

        ColheitaResponse resposta =
                service.registrar(request(1L), EMAIL);

        assertThat(resposta.dataColheita())
                .isEqualTo(HOJE);

        assertThat(resposta.talhaoCodigo())
                .isEqualTo("A1");

        assertThat(resposta.quantidade())
                .isEqualByComparingTo("500");

        assertThat(resposta.usuarioId())
                .isEqualTo(7L);

        verify(colheitaRepository)
                .save(any(Colheita.class));
    }

    @Test
    void registrar_comLiberacaoPassada_eAceita() {

        talhaoComLiberacao(HOJE.minusDays(3));
        esperaSalvarEDevolver();

        when(usuario.getId()).thenReturn(7L);

        ColheitaResponse resposta =
                service.registrar(request(1L), EMAIL);

        assertThat(resposta.dataColheita())
                .isEqualTo(HOJE);

        assertThat(resposta.quantidade())
                .isEqualByComparingTo("500");
    }

    @Test
    void registrar_semDataDeLiberacao_eAceita() {

        talhaoComLiberacao(null);
        esperaSalvarEDevolver();

        when(usuario.getId()).thenReturn(7L);

        ColheitaResponse resposta =
                service.registrar(request(1L), EMAIL);

        assertThat(resposta.dataColheita())
                .isEqualTo(HOJE);

        assertThat(resposta.quantidade())
                .isEqualByComparingTo("500");
    }

    @Test
    void registrar_comTalhaoInexistente_recusaCom422() {

        when(talhaoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.registrar(request(99L), EMAIL)
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        ex -> assertThat(ex.getStatus())
                                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                );

        verify(colheitaRepository, never())
                .save(any(Colheita.class));
    }

    @Test
    void registrar_comQuantidadeZero_recusa() {

        talhaoComLiberacao(null);

        ColheitaRequest request = new ColheitaRequest(
                1L,
                HOJE,
                BigDecimal.ZERO
        );

        assertThatThrownBy(() ->
                service.registrar(request, EMAIL)
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        ex -> assertThat(ex.getStatus())
                                .isEqualTo(HttpStatus.BAD_REQUEST)
                );

        verify(colheitaRepository, never())
                .save(any(Colheita.class));
    }

    @Test
    void registrar_semData_recusa() {

        talhaoComLiberacao(null);

        ColheitaRequest request = new ColheitaRequest(
                1L,
                null,
                new BigDecimal("500")
        );

        assertThatThrownBy(() ->
                service.registrar(request, EMAIL)
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        ex -> assertThat(ex.getStatus())
                                .isEqualTo(HttpStatus.BAD_REQUEST)
                );

        verify(colheitaRepository, never())
                .save(any(Colheita.class));
    }
}
