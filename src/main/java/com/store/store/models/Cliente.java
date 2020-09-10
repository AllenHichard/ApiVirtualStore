package com.store.store.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TB_Cliente")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1l;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String nome;
	private String login;
	private String senha;
	
	@OneToMany
	private List<Pedido> pedidos;
	
	public void addPedido(Pedido pedido) {
		pedidos.add(pedido);
	}
	
	public void removerPedido(Pedido pedido) {
		pedido.removerTodosProdutos(pedido.getProdutos());
		pedidos.remove(pedido);
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
