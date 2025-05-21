package com.genetions.lojadegames.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_categoria")
public class Categoria {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    @NotBlank(message = "O atributo descricao é obrigatório!")
    @Size(max = 1000, message = "O atributo descricao deve ter no mínimo 10 caracteres e no máximo 1000 caracteres.")
    private String descricao;
    
    @Column(length = 100)
    @NotBlank(message = "O atributo nome é obrigatório!")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9\\s]*$", message = "O nome deve começar com uma letra e conter apenas letras, números ou espaços")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 5 e 100 caracteres.")
    private String nome;
    
    public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL) 
    @JsonIgnoreProperties("categoria")
    private List<Produto> produtos;
    
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
