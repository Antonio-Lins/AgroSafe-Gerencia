package br.com.agrosafe.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "talhoes")
public class Talhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal hectares;

    @Column(nullable = false, length = 100)
    private String cultura;

    /** Derivada das aplicações não canceladas (RN03) e guardada por conveniência (seção 9.3). */
    @Column(name = "data_liberacao")
    private LocalDate dataLiberacao;

    protected Talhao() {
        // exigido pelo JPA
    }

    public Talhao(String codigo, BigDecimal hectares, String cultura) {
        this.codigo = codigo;
        this.hectares = hectares;
        this.cultura = cultura;
    }

    /** RN01: Livre se a data de liberação é nula ou menor ou igual a hoje; senão, Em quarentena. */
    public EstadoTalhao estadoEm(LocalDate hoje) {
        if (dataLiberacao == null || !dataLiberacao.isAfter(hoje)) {
            return EstadoTalhao.LIVRE;
        }
        return EstadoTalhao.EM_QUARENTENA;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getHectares() {
        return hectares;
    }

    public void setHectares(BigDecimal hectares) {
        this.hectares = hectares;
    }

    public String getCultura() {
        return cultura;
    }

    public void setCultura(String cultura) {
        this.cultura = cultura;
    }

    public LocalDate getDataLiberacao() {
        return dataLiberacao;
    }

    public void setDataLiberacao(LocalDate dataLiberacao) {
        this.dataLiberacao = dataLiberacao;
    }

}
