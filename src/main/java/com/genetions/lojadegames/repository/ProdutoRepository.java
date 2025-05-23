package com.genetions.lojadegames.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.genetions.lojadegames.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findAllByNomeContainingIgnoreCase(String nome);
    List<Produto> findAllByPrecoBetween(BigDecimal min, BigDecimal max);
    boolean existsByCategoriaId(Long categoriaId);
}