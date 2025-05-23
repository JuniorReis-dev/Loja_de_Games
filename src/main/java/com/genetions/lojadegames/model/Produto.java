package com.genetions.lojadegames.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @NotBlank(message = "O atributo nome é obrigatório!")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9\\s]*$", message = "O nome deve começar com uma letra e conter apenas letras, números ou espaços")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotNull(message = "O preço não pode ser nulo!")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero!")
    private BigDecimal preco;

    @Column(nullable = false)
    @NotNull(message = "O link da foto não pode ser nulo!")
    @NotBlank(message = "O link da foto é obrigatório!")
    @Size(max = 255, message = "O link da foto deve ter no máximo 255 caracteres.")
    private String foto;

    @ManyToOne
    @JsonIgnoreProperties("produtos")
    @NotNull(message = "A categoria é obrigatória!")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties("produtos")
    @NotNull(message = "O usuário é obrigatório!")
    private Usuario usuario;

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}