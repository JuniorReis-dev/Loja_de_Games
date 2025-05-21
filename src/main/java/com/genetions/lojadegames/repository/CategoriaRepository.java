package com.genetions.lojadegames.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genetions.lojadegames.model.Categoria;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	 List<Categoria> findAllByNomeContainingIgnoreCase(String nome);
    // Este método encontrará categorias pela descrição
    List<Categoria> findAllByDescricaoContainingIgnoreCase(String descricao);
}
