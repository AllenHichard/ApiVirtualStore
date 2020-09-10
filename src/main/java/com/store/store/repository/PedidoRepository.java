package com.store.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.store.models.Pedido;
import com.store.store.models.Produto;

// <classe e tipo do is>
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	Pedido findById(long id);
	
}
