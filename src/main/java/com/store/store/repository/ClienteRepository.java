package com.store.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.store.store.models.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {
	Cliente findById(long id);
}
