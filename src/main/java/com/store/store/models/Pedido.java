package com.store.store.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TB_Pedido")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1l;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private Date data;
	private double valorTotal;
	//muitos pedidos um produto
	@OneToMany 
	private List<Produto> produtos;
	
	public void addProduto(Produto produto) {
		this.valorTotal += produto.getQuantidade()*produto.getValor();
		produtos.add(produto);
	}
	
	
	public void removerProduto(Produto produto) {
		this.valorTotal -= produto.getQuantidade()*produto.getValor();
		produtos.remove(produto);
	}
	
	
	public void removerTodosProdutos(List<Produto> produtos) {
		this.produtos.removeAll(produtos);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}


	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	
	

}
