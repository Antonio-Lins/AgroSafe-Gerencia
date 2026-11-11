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
import java.time.LocalDateTime;

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

    // Usuário que registrou a aplicação (RF02)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "data_liberacao_calculada", nullable = false)
    private LocalDate dataLiberacaoCalculada;

    @Column(nullable = false)
    private boolean cancelada = false;

    @Column(name = "motivo_cancelamento", length = 500)
    private String motivoCancelamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelada_por_id")
    private Usuario canceladaPor;

    @Column(name = "cancelada_em")
    private LocalDateTime canceladaEm;

    protected Aplicacao() {
        // exigido pelo JPA
    }

    public Aplicacao(Talhao talhao, Defensivo defensivo, Usuario usuario,
                     LocalDate dataAplicacao, LocalDate dataLiberacaoCalculada) {
        this.talhao = talhao;
        this.defensivo = defensivo;
        this.usuario = usuario;
        this.dataAplicacao = dataAplicacao;
        this.dataLiberacaoCalculada = dataLiberacaoCalculada;
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

    public Usuario getUsuario() {
        return usuario;
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

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public Usuario getCanceladaPor() {
        return canceladaPor;
    }

    public LocalDateTime getCanceladaEm() {
        return canceladaEm;
    }
}
