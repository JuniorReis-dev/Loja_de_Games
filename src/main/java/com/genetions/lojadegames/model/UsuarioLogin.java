package com.genetions.lojadegames.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioLogin {

    private Long id;

    @NotBlank(message = "O usuário é obrigatório!")
    @Email(message = "O usuário deve ser um email válido!")
    private String usuario;

    @NotBlank(message = "A senha é obrigatória!")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    private String nome;
    private String foto;
    private String token;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}