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
@Table(name = "aplicacoes")
public class Aplicacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "talhao_id")
    private Talhao talhao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "defensivo_id")
    private Defensivo defensivo;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "data_liberacao_calculada", nullable = false)
    private LocalDate dataLiberacaoCalculada;

    @Column(nullable = false)
    private boolean cancelada = false;

    protected Aplicacao() {
        // exigido pelo JPA
    }

    public Long getId() {
        return id;
    }

    public Talhao getTalhao() {
        return talhao;
    }

    public Defensivo getDefensivo() {
        return defensivo;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public LocalDate getDataLiberacaoCalculada() {
        return dataLiberacaoCalculada;
    }

    public boolean isCancelada() {
        return cancelada;
    }
}
