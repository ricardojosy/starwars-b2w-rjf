package com.starwars.api.services;

import java.util.List;

import com.starwars.api.documents.Planeta;

public interface PlanetaService {
	
	List<Planeta> listarTodos();
	
	Planeta buscarPorId(String id);
	
	Planeta buscarPorNome(String nome);
	
	Planeta cadastrar(Planeta planeta);
	
	Planeta atualizar(Planeta planeta);
	
	void remover(String id);

}
