package br.com.agrosafe.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "defensivos")
public class Defensivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_comercial", nullable = false, length = 150)
    private String nomeComercial;

    @Column(name = "principio_ativo", nullable = false, length = 150)
    private String principioAtivo;

    /** Número mínimo de dias entre a aplicação e a colheita (inteiro maior que zero). */
    @Column(name = "dias_carencia", nullable = false)
    private Integer diasCarencia;

    protected Defensivo() {
        // exigido pelo JPA
    }

    public Defensivo(String nomeComercial, String principioAtivo, Integer diasCarencia) {
        this.nomeComercial = nomeComercial;
        this.principioAtivo = principioAtivo;
        this.diasCarencia = diasCarencia;
    }

    public Long getId() {
        return id;
    }

    public String getNomeComercial() {
        return nomeComercial;
    }

    public void setNomeComercial(String nomeComercial) {
        this.nomeComercial = nomeComercial;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public Integer getDiasCarencia() {
        return diasCarencia;
    }

    public void setDiasCarencia(Integer diasCarencia) {
        this.diasCarencia = diasCarencia;
    }

}
