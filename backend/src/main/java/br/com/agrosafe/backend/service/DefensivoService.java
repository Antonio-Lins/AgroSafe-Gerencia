package br.com.agrosafe.backend.service;

import br.com.agrosafe.backend.dto.request.DefensivoRequest;
import br.com.agrosafe.backend.dto.response.DefensivoResponse;
import br.com.agrosafe.backend.exception.BusinessException;
import br.com.agrosafe.backend.exception.ResourceNotFoundException;
import br.com.agrosafe.backend.model.Defensivo;
import br.com.agrosafe.backend.repository.AplicacaoRepository;
import br.com.agrosafe.backend.repository.DefensivoRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefensivoService {
    private final DefensivoRepository defensivoRepository;
    private final AplicacaoRepository aplicacaoRepository;

    public DefensivoService(DefensivoRepository defensivoRepository,
                            AplicacaoRepository aplicacaoRepository) {
        this.defensivoRepository = defensivoRepository;
        this.aplicacaoRepository = aplicacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<DefensivoResponse> listar() {
        return defensivoRepository.findAll(Sort.by("nomeComercial")).stream()
                .map(DefensivoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public DefensivoResponse buscar(Long id) {
        return DefensivoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public DefensivoResponse criar(DefensivoRequest request) {
        Defensivo defensivo = new Defensivo(
                request.nomeComercial().strip(),
                request.principioAtivo().strip(),
                request.diasCarencia());
        return DefensivoResponse.de(defensivoRepository.saveAndFlush(defensivo));
    }

    @Transactional
    public DefensivoResponse atualizar(Long id, DefensivoRequest request) {
        Defensivo defensivo = buscarEntidade(id);
        defensivo.setNomeComercial(request.nomeComercial().strip());
        defensivo.setPrincipioAtivo(request.principioAtivo().strip());
        defensivo.setDiasCarencia(request.diasCarencia());
        return DefensivoResponse.de(defensivoRepository.saveAndFlush(defensivo));
    }

    @Transactional
    public void remover(Long id) {
        Defensivo defensivo = buscarEntidade(id);
        if (aplicacaoRepository.existsByDefensivoId(id)) {
            throw new BusinessException("Não é possível excluir o defensivo \""
                    + defensivo.getNomeComercial()
                    + "\": há aplicações registradas e o histórico deve ser preservado.");
        }
        defensivoRepository.delete(defensivo);
    }

    private Defensivo buscarEntidade(Long id) {
        return defensivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Defensivo não encontrado."));
    }
}
