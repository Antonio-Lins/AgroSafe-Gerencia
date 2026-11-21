package br.com.agrosafe.backend.talhao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TalhaoRepository extends JpaRepository<Talhao,Long> {
    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
