package br.com.agrosafe.backend.talhao;

import br.com.agrosafe.backend.talhao.dto.TalhaoRequest;
import br.com.agrosafe.backend.talhao.dto.TalhaoResponse;
import br.com.agrosafe.backend.shared.exception.BusinessException;
import br.com.agrosafe.backend.shared.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.aplicacao.AplicacaoRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TalhaoService {
    private final TalhaoRepository talhaoRepository;
    private final AplicacaoRepository aplicacaoRepository;
    private final Clock clock;

    public TalhaoService(TalhaoRepository talhaoRepository,
                         AplicacaoRepository aplicacaoRepository,
                         Clock clock) {
        this.talhaoRepository = talhaoRepository;
        this.aplicacaoRepository = aplicacaoRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<TalhaoResponse> listar() {
        LocalDate hoje = LocalDate.now(clock);
        return talhaoRepository.findAll(Sort.by("codigo")).stream()
                .map(talhao -> TalhaoResponse.de(talhao, hoje))
                .toList();
    }

    @Transactional(readOnly = true)
    public TalhaoResponse buscar(Long id) {
        return TalhaoResponse.de(buscarEntidade(id), LocalDate.now(clock));
    }

    @Transactional
    public TalhaoResponse criar(TalhaoRequest request) {
        String codigo = request.codigo().strip();
        if (talhaoRepository.existsByCodigo(codigo)) {
            throw codigoEmUso(codigo);
        }
        Talhao talhao = new Talhao(codigo, request.hectares(), request.cultura().strip());
        return TalhaoResponse.de(talhaoRepository.saveAndFlush(talhao), LocalDate.now(clock));
    }

    @Transactional
    public TalhaoResponse atualizar(Long id, TalhaoRequest request) {
        Talhao talhao = buscarEntidade(id);
        String codigo = request.codigo().strip();
        if (talhaoRepository.existsByCodigoAndIdNot(codigo, id)) {
            throw codigoEmUso(codigo);
        }
        talhao.setCodigo(codigo);
        talhao.setHectares(request.hectares());
        talhao.setCultura(request.cultura().strip());
        return TalhaoResponse.de(talhaoRepository.saveAndFlush(talhao), LocalDate.now(clock));
    }

    @Transactional
    public void remover(Long id) {
        Talhao talhao = buscarEntidade(id);
        if (aplicacaoRepository.existsByTalhaoId(id)) {
            throw new BusinessException("Não é possível excluir o talhão \"" + talhao.getCodigo()
                    + "\": há aplicações registradas e o histórico deve ser preservado.");
        }
        talhaoRepository.delete(talhao);
    }

    private Talhao buscarEntidade(Long id) {
        return talhaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talhão não encontrado."));
    }

    private BusinessException codigoEmUso(String codigo) {
        return new BusinessException("O código \"" + codigo + "\" já está em uso.");
    }
}
