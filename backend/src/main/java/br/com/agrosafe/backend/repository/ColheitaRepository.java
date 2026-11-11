package br.com.agrosafe.backend.repository;

import br.com.agrosafe.backend.model.Colheita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColheitaRepository extends JpaRepository<Colheita, Long> {
}
