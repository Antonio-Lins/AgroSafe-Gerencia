
package br.com.agrosafe.backend.colheita;

import br.com.agrosafe.backend.talhao.Talhao;
import br.com.agrosafe.backend.usuario.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private boolean cancelada = false;

    @Column(name = "motivo_cancelamento", length = 500)
    private String motivoCancelamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelada_por_id")
    private Usuario canceladaPor;

    @Column(name = "cancelada_em")
    private LocalDateTime canceladaEm;

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

    // US-22: registra o cancelamento sem excluir a colheita (o registro original é preservado).
    public void cancelar(String motivo, Usuario responsavel, LocalDateTime dataHora) {
        this.cancelada = true;
        this.motivoCancelamento = motivo;
        this.canceladaPor = responsavel;
        this.canceladaEm = dataHora;
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
