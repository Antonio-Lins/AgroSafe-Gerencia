package br.com.agrosafe.backend.aplicacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long>{
    // RF01: talhão com aplicações (inclusive canceladas) não pode ser excluído.
    boolean existsByTalhaoId(Long talhaoId);

    //RF01: defensivo com aplicações (inclusive canceladas) não pode ser excluído.
    boolean existsByDefensivoId(Long defensivoId);

    // US-21: maior data de liberação entre as aplicações ativas.
    @Query("""
            SELECT MAX(a.dataLiberacaoCalculada)
            FROM Aplicacao a
            WHERE a.talhao.id = :talhaoId
              AND a.cancelada = false
            """)
    LocalDate buscarMaiorDataLiberacaoAtiva(
            @Param("talhaoId") Long talhaoId
    );
}
