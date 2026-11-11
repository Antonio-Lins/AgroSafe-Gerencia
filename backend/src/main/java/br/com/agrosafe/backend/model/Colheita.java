package br.com.agrosafe.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "colheitas")
public class Colheita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "talhao_id")
    private Talhao talhao;

    // Usuário que registrou a colheita
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "data_colheita", nullable = false)
    private LocalDate dataColheita;

    protected Colheita() {
        // exigido pelo JPA
    }

    public Colheita(Talhao talhao, Usuario usuario, LocalDate dataColheita) {
        this.talhao = talhao;
        this.usuario = usuario;
        this.dataColheita = dataColheita;
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
}
