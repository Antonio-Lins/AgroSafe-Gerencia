package br.com.agrosafe.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.agrosafe.backend.dto.request.DefensivoRequest;
import br.com.agrosafe.backend.dto.response.DefensivoResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.model.Defensivo;
import br.com.agrosafe.backend.repository.AplicacaoRepository;
import br.com.agrosafe.backend.repository.DefensivoRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class DefensivoServiceTest {
    @Mock
    private DefensivoRepository defensivoRepository;

    @Mock
    private AplicacaoRepository aplicacaoRepository;

    private DefensivoService service;

    @BeforeEach
    void setUp() {
        service = new DefensivoService(defensivoRepository, aplicacaoRepository);
    }

    @Test
    void criar_comDadosValidos_salvaODefensivo() {
        when(defensivoRepository.saveAndFlush(any(Defensivo.class))).thenAnswer(inv -> inv.getArgument(0));

        DefensivoResponse resposta = service.criar(new DefensivoRequest(" Produto A ", "Glifosato", 15));

        assertThat(resposta.nomeComercial()).isEqualTo("Produto A");
        assertThat(resposta.principioAtivo()).isEqualTo("Glifosato");
        assertThat(resposta.diasCarencia()).isEqualTo(15);
        verify(defensivoRepository).saveAndFlush(any(Defensivo.class));
    }

    @Test
    void atualizar_comDadosValidos_alteraOsCampos() {
        Defensivo existente = new Defensivo("Produto A", "Glifosato", 15);
        when(defensivoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(defensivoRepository.saveAndFlush(existente)).thenReturn(existente);

        DefensivoResponse resposta = service.atualizar(1L, new DefensivoRequest("Produto A", "Atrazina", 20));

        assertThat(resposta.principioAtivo()).isEqualTo("Atrazina");
        assertThat(resposta.diasCarencia()).isEqualTo(20);
    }

    @Test
    void atualizar_defensivoInexistente_lancaNaoEncontrado() {
        when(defensivoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99L, new DefensivoRequest("Produto A", "Glifosato", 15)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void remover_semAplicacoes_removeODefensivo() {
        Defensivo defensivo = new Defensivo("Produto A", "Glifosato", 15);
        when(defensivoRepository.findById(1L)).thenReturn(Optional.of(defensivo));
        when(aplicacaoRepository.existsByDefensivoId(1L)).thenReturn(false);

        service.remover(1L);

        verify(defensivoRepository).delete(defensivo);
    }

    @Test
    void remover_comAplicacoes_lancaConflitoENaoRemove() {
        when(defensivoRepository.findById(1L))
                .thenReturn(Optional.of(new Defensivo("Produto A", "Glifosato", 15)));
        when(aplicacaoRepository.existsByDefensivoId(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.remover(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("histórico");
        verify(defensivoRepository, never()).delete(any(Defensivo.class));
    }
}
