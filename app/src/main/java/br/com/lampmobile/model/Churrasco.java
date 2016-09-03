package br.com.lampmobile.model;

import br.com.lampmobile.helper.ChurrascoHelper;

public class Churrasco {
    private Integer id;
    private String item;
    private ChurrascoHelper.Tipo tipo;
    private Boolean ativo;
    private String resultado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public ChurrascoHelper.Tipo getTipo() {
        return tipo;
    }

    public void setTipo(ChurrascoHelper.Tipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
