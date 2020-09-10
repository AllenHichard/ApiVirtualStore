package com.store.store.resources;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.store.input.ClientePedido;
import com.store.store.input.EstoqueProduto;
import com.store.store.input.PedidoProduto;
import com.store.store.models.Cliente;
import com.store.store.models.Estoque;
import com.store.store.models.Pedido;
import com.store.store.models.Produto;
import com.store.store.repository.ClienteRepository;
import com.store.store.repository.EstoqueRepository;
import com.store.store.repository.PedidoRepository;
import com.store.store.repository.ProdutoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api")
@Api(value="API REST Produtos")


@CrossOrigin(origins="*")
public class PedidoResource {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	EstoqueRepository estoqueRepository;
	
	/**
	 * The addCarrinho method is responsible for adding orders to the customer's cart
	 * @param ep
	 * @return
	 */
	@PostMapping("/addCarrinho")
	@ApiOperation(value="This route serves to add products from stock in the customer's purchase")
	public Object salvaProduto(@RequestBody EstoqueProduto ep){
		Estoque estoque = estoqueRepository.findById(ep.getIdEstoque());
		Pedido pedido = pedidoRepository.findById(ep.getIdPedido());
		if(estoque == null) return "{Error: Estoque Inexistente}" ;
		else if(pedido == null) return "{Error: Pedido Inexistente}" ;
		Produto p = new Produto();
		p.setNome(estoque.getNome());
		p.setQuantidade(ep.getQtd());
		p.setValor(estoque.getValor());
		if(estoque.getQuantidade() >= ep.getQtd()) {
			estoque.setQuantidade(estoque.getQuantidade() - ep.getQtd());
			pedido.addProduto(p);
			produtoRepository.save(p);
			estoqueRepository.save(estoque);
			pedidoRepository.save(pedido);
		}
		return p;
	}
	/**
	 * This method is used to save a customer order
	 * @param id
	 * @param pedido
	 * @return
	 */
	@PostMapping("/pedido/{id}")
	@ApiOperation(value="This route has the function of initializing the cart for a customer")
	public Pedido salvaPedido(@PathVariable("id") long id,@RequestBody Pedido pedido){
		Cliente cliente = clienteRepository.findById(id);
		if(cliente == null) return null;
		cliente.addPedido(pedido);
		return pedidoRepository.save(pedido);
	}
	
	@PostMapping("/cliente")
	@ApiOperation(value="This route is to save a new customer")
	public Cliente salvaCliente(@RequestBody Cliente cliente){
		return clienteRepository.save(cliente);
	}
	
	@PostMapping("/estoque")
	@ApiOperation(value="This route is to save a new product in stock")
	public Estoque salvaEstoque(@RequestBody Estoque estoque){
		return estoqueRepository.save(estoque);
	}
	
	@GetMapping("/pedidos")
	@ApiOperation(value="This route returns the list of orders registered")
	public List<Pedido> listaPedidos(){
		return pedidoRepository.findAll();
	}
	
	@GetMapping("/produtos")
	@ApiOperation(value="This route returns the registered product list")
	public Iterable<Produto> listaProdutos(){
		return produtoRepository.findAll();
	}
	@GetMapping("/clientes")
	@ApiOperation(value="This route returns the list of registered customers")
	public Iterable<Cliente> listaClientes(){
		return clienteRepository.findAll();
	}
	
	@GetMapping("/estoque")
	@ApiOperation(value="This route returns the list of products registered in the stock")
	public Iterable<Estoque> listaEstoque(){
		return estoqueRepository.findAll();
	}

	@DeleteMapping("/removerProduto")
	@ApiOperation(value="This route completely deletes an order, removing products from the cart")
	public String deletaPedido(@RequestBody  PedidoProduto pp){
		Pedido pedido = pedidoRepository.findById(pp.getIdPedido());
		Produto produto = produtoRepository.findById(pp.getIdProduto());
		if(produto == null) return "{Error: Produto Inexistente}";
		else if(pedido == null) return "{Error: Pedido Inexistente}";
		pedido.removerProduto(produto);
		pedidoRepository.save(pedido);
		Estoque estoque = estoqueRepository.findByNome(produto.getNome());
		estoque.setQuantidade(estoque.getQuantidade() + produto.getQuantidade());
		estoqueRepository.save(estoque);
		return "{Removido do carrinho: Adicione outro produto}";
	}
	
	
	@DeleteMapping("/resetarCarrinho")
	@ApiOperation(value="This clean route, that is, resets the shopping cart")
	public String resetarCarrinho(@RequestBody  ClientePedido cp){
		Cliente cliente = clienteRepository.findById(cp.getIdCliente());
		Pedido pedido = pedidoRepository.findById(cp.getIdPedido());
		if(pedido == null) return "{Error: Pedido Inexistente}";
		else if(cliente == null) return "{Error: Cliente Inexistente}";
		
		List produtos = pedido.getProdutos();
		Iterator <Produto> p = produtos.iterator();
		while(p.hasNext()) {
			Produto prod = (Produto) p.next();
			Estoque estoque = estoqueRepository.findByNome(prod.getNome());
			estoque.setQuantidade(estoque.getQuantidade() + prod.getQuantidade());
			estoqueRepository.save(estoque);
		}
		cliente.removerPedido(pedido);
		clienteRepository.save(cliente);
		pedidoRepository.delete(pedido);
		return "{Carrinho removido: Adicione outro produto}";
	}
	
	@GetMapping("/pedido/{id}")
	@ApiOperation(value="Returns a request by ID")
	public Pedido getPedidoID(@PathVariable(value="id") long id){
		return pedidoRepository.findById(id);
	}
	@GetMapping("/produto/{id}")
	@ApiOperation(value="Returns a product by ID")
	public Produto getProdutoID(@PathVariable(value="id") long id){
		return produtoRepository.findById(id);
	}
	@GetMapping("/cliente/{id}")
	@ApiOperation(value="Returns a customer by ID")
	public Cliente getClienteID(@PathVariable(value="id") long id){
		return clienteRepository.findById(id);
	}
	
	@PutMapping("/produto")
	@ApiOperation(value="Updates data for a specific product")
	public Produto atualizaProduto(@RequestBody Produto produto){
		return produtoRepository.save(produto);
	}
	@PutMapping("/pedido")
	@ApiOperation(value="Updates data for a specific order")
	public Pedido atualizaPedido(@RequestBody Pedido pedido){
		return pedidoRepository.save(pedido);
	}
	@PutMapping("/cliente")
	@ApiOperation(value="Updates data for a specific customer")
	public Cliente atualizaCliente(@RequestBody Cliente cliente){
		return clienteRepository.save(cliente);
	}
	
}



