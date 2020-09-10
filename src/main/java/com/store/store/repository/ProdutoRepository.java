package com.store.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.store.store.models.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Long>{

	Produto findById(long id);

}
