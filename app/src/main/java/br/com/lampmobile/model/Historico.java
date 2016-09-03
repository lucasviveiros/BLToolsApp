package br.com.lampmobile.model;

import java.util.Date;

public class Historico {
    private Integer id;
    private String titulo;
    private Object[] params;
    private Boolean favorito;
    private Date dataInclusao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Historico) {
            return id.equals(((Historico) o).getId());
        }
        
        return false;
    }
}
