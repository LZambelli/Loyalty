package joaopogiolli.com.br.loyalty.Models;

import java.io.Serializable;

/**
 * Created by Joao Poggioli on 25/02/2018.
 */

public class Cartao implements Serializable{

    private String id;
    private String idUsuario;
    private String idPromocao;
    private int qntCarimbos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdPromocao() {
        return idPromocao;
    }

    public void setIdPromocao(String idPromocao) {
        this.idPromocao = idPromocao;
    }

    public int getQntCarimbos() {
        return qntCarimbos;
    }

    public void setQntCarimbos(int qntCarimbos) {
        this.qntCarimbos = qntCarimbos;
    }
}
