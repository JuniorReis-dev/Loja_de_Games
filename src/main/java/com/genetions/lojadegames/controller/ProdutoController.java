package com.genetions.lojadegames.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.genetions.lojadegames.model.Produto;
import com.genetions.lojadegames.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	    @Autowired
	    private ProdutoRepository produtoRepository;

	    @GetMapping
	    public ResponseEntity<List<Produto>> getAll() {
	        return ResponseEntity.ok(produtoRepository.findAll());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Produto> getById(@PathVariable Long id) {
	        return produtoRepository.findById(id)
	                .map(ResponseEntity::ok)
	                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	    }

	    @GetMapping("/nome/{nome}")
	    public ResponseEntity<List<Produto>> getAllByNome(@PathVariable String nome) {
	        return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	    }

	    @GetMapping("/preco/{min}/{max}")
	    public ResponseEntity<List<Produto>> getByPrecoBetween(
	            @PathVariable BigDecimal min,
	            @PathVariable BigDecimal max) {
	        return ResponseEntity.ok(produtoRepository.findAllByPrecoBetween(min, max));
	    }

	    @PostMapping
	    public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
	        return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	    }

	    @PutMapping
	    public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
	        if (produto.getId() == null) {
	            return ResponseEntity.badRequest().build();
	        }

	        if (produtoRepository.existsById(produto.getId())) {
	            return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
	        }

	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    @DeleteMapping("/{id}")
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    public void delete(@PathVariable Long id) {
	        Optional<Produto> produto = produtoRepository.findById(id);

	        if (produto.isEmpty()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!");
	        }

	        produtoRepository.deleteById(id);
	    }
}