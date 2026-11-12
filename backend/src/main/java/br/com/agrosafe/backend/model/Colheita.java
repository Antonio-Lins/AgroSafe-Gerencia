
package br.com.agrosafe.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "colheitas")
public class Colheita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "talhao_id", nullable = false)
    private Talhao talhao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_colheita", nullable = false)
    private LocalDate dataColheita;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantidade;

    protected Colheita() {
    }

    public Colheita(
            Talhao talhao,
            Usuario usuario,
            LocalDate dataColheita,
            BigDecimal quantidade) {

        this.talhao = talhao;
        this.usuario = usuario;
        this.dataColheita = dataColheita;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public Talhao getTalhao() {
        return talhao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataColheita() {
        return dataColheita;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }
}
