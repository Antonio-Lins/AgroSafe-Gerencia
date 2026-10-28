package br.com.agrosafe.backend.repository;

import br.com.agrosafe.backend.model.Talhao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalhaoRepository extends JpaRepository<Talhao,Long> {
    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
