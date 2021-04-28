package com.starwars.api.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.starwars.api.documents.Planeta;
import com.starwars.api.responses.Response;
import com.starwars.api.services.PlanetaService;

@RestController
@RequestMapping(path = "/api/planetas")
public class PlanetaController {

	@Autowired
	private PlanetaService planetaService;

	@GetMapping
	public ResponseEntity<Response<List<Planeta>>> listarTodos() {
		return ResponseEntity.ok(new Response<List<Planeta>>(this.planetaService.listarTodos()));
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Response<Planeta>> buscarPorId(@PathVariable(name = "id") String id) {
		Planeta planeta = this.planetaService.buscarPorId(id);
		return planeta != null ? ResponseEntity.ok(new Response<Planeta>(planeta)) : ResponseEntity.notFound().build();
	}

	@GetMapping("/por-nome")
	public ResponseEntity<Response<Planeta>> buscarPorNome(@RequestParam String nome) {
		Planeta planeta = this.planetaService.buscarPorNome(nome);
		return planeta != null ?  ResponseEntity.ok(new Response<Planeta>(planeta)) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Response<Planeta>> cadastrar(@Valid @RequestBody Planeta planeta, BindingResult result) {
		if (result.hasErrors()) {
			List<String> erros = new ArrayList<String>();
			result.getAllErrors().forEach(erro -> erros.add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(new Response<Planeta>(erros));
		}

		ResponseEntity<Response<Planeta>> re = this.buscarPorNome(planeta.getNome());
		if (re.getStatusCodeValue() == HttpStatus.SC_OK) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(new Response<Planeta>(this.planetaService.cadastrar(planeta)));
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Response<Planeta>> atualizar(@PathVariable(name = "id") String id,
			@Valid @RequestBody Planeta planeta, BindingResult result) {
		if (result.hasErrors()) {
			List<String> erros = new ArrayList<String>();
			result.getAllErrors().forEach(erro -> erros.add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(new Response<Planeta>(erros));
		}

		ResponseEntity<Response<Planeta>> re = this.buscarPorId(id);
		if (re.getStatusCodeValue() == HttpStatus.SC_NOT_FOUND) {
			return ResponseEntity.notFound().build();
		}

		planeta.setId(id);
		return ResponseEntity.ok(new Response<Planeta>(this.planetaService.atualizar(planeta)));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Response<Integer>> remover(@PathVariable(name = "id") String id) {
		ResponseEntity<Response<Planeta>> planeta = this.buscarPorId(id);
		if (planeta.getStatusCodeValue() == HttpStatus.SC_NOT_FOUND) {
			return ResponseEntity.notFound().build();
		}

		this.planetaService.remover(id);
		return ResponseEntity.ok(new Response<Integer>(1));
	}

}
