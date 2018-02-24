package joaopogiolli.com.br.loyalty.Models;

import java.io.Serializable;

/**
 * Created by jlago on 20/02/2018.
 */

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String celular;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
