package joaopogiolli.com.br.loyalty.Models;

import java.io.Serializable;

/**
 * Created by Joao Poggioli on 22/02/2018.
 */

public class Promocao implements Serializable {

    private String id;
    private String idEstabelecimento;
    private String titulo;
    private String descricao;
    private String dataCricao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(String idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataCricao() {
        return dataCricao;
    }

    public void setDataCricao(String dataCricao) {
        this.dataCricao = dataCricao;
    }
}
