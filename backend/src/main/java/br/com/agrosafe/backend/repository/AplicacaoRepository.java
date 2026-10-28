package br.com.agrosafe.backend.repository;

import br.com.agrosafe.backend.model.Aplicacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long>{
    // RF01: talhão com aplicações (inclusive canceladas) não pode ser excluído.
    boolean existsByTalhaoId(Long talhaoId);

    //RF01: defensivo com aplicações (inclusive canceladas) não pode ser excluído.
    boolean existsByDefensivoId(Long defensivoId);
}
