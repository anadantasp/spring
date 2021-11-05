package br.org.generation.lojagames.controller;

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

import br.org.generation.lojagames.model.Categoria;
import br.org.generation.lojagames.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//Traz todas as categorias
	@GetMapping
	public ResponseEntity <List<Categoria>> getAll ()
	{
		return ResponseEntity.ok(categoriaRepository.findAll());
	}
	
	//Traz uma única categoria por id
	@GetMapping("/{id}")
	public ResponseEntity <Categoria> getById (@PathVariable long id)
	{
		return categoriaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	//Traz categorias pelo nome
	@GetMapping("/categoria/{categoria}")
	public ResponseEntity <List<Categoria>> getByCategoria (@PathVariable String categoria)
	{
		return ResponseEntity.ok(categoriaRepository.findAllByCategoriaContainingIgnoreCase(categoria));
	}
	
	//Grava uma nova categoria no banco de dados
	@PostMapping
	public ResponseEntity <Categoria> postCategoria (@Valid @RequestBody Categoria categoria)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
	}
	
	//Atualiza dados de uma categoria
	@PutMapping
	public ResponseEntity <Categoria> putCategoria (@Valid @RequestBody Categoria categoria)
	{
		return categoriaRepository.findById(categoria.getId())
				.map(resposta -> 
				{ return ResponseEntity.ok().body(categoriaRepository.save(categoria));
				})
				.orElse (ResponseEntity.notFound().build());
	
	}
	
	//Deleta uma categoria pelo id no banco de dados
	@DeleteMapping("/{id}")
	public ResponseEntity <?> deleteCategoria (@PathVariable long id)
	{
		return categoriaRepository.findById(id)
		.map(resposta -> {
			categoriaRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		}) 
		.orElse(ResponseEntity.notFound().build());
		
	}
}
