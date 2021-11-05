package br.org.generation.lojagames.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import br.org.generation.lojagames.model.Produto;
import br.org.generation.lojagames.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	ProdutoRepository produtoRepository;
	
	//Traz todos os produtos
	@GetMapping
	public ResponseEntity <List<Produto>> getAll ()
	{
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	//Traz um único produto por id
	@GetMapping("/{id}")
	public ResponseEntity <Produto> getById (@PathVariable long id)
	{
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	//Traz produtos pelo nome
	@GetMapping("/nome/{nome}")
	public ResponseEntity <List<Produto>> findByNome (@PathVariable String nome)
	{
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	//Traz produtos pelo preço igual e maior do que digitado
	@GetMapping("/preco_maior/{preco}")
	public ResponseEntity <List<Produto>> findByPrecoMaiorIgualQue (@PathVariable BigDecimal preco)
	{
		return ResponseEntity.ok(produtoRepository.findAllByPrecoGreaterThanEqualOrderByPreco(preco));
	}
	
	//Traz produtos pelo preço igual e menor do que o preço digitado 
	@GetMapping("/preco_menor/{preco}")
	public ResponseEntity <List<Produto>> findByPrecoMenorIgualQue (@PathVariable BigDecimal preco)
	{
		return ResponseEntity.ok(produtoRepository.findAllByPrecoLessThanEqualOrderByPrecoDesc(preco));
	}
	
	//Grava um novo produto no banco de dados
	@PostMapping
	public ResponseEntity <Produto> postProduto (@Valid @RequestBody Produto produto)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	}
	
	//Atualiza dados de um produto 
	@PutMapping
	public ResponseEntity <Produto> putProduto (@Valid @RequestBody Produto produto)
	{
		return produtoRepository.findById(produto.getId())
				.map(resposta -> {
					return ResponseEntity.ok().body(produtoRepository.save(produto));
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	//Apaga um produto pelo id no banco de dados
	@DeleteMapping("/{id}")
	public ResponseEntity <?> deleteProduto (@PathVariable long id)
	{
		return produtoRepository.findById(id)
				.map(resposta -> {
					produtoRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
}
