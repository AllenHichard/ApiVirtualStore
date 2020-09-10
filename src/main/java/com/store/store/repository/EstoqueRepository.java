package com.store.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.store.store.models.Estoque;

public interface EstoqueRepository extends CrudRepository<Estoque, Long>{

	Estoque findById(long id);
	Estoque findByNome(String nome);

}
