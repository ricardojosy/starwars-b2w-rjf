package com.starwars.api.services.impl;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starwars.api.documents.Planeta;
import com.starwars.api.planet.swapi.Planet;
import com.starwars.api.repositories.PlanetaRepository;
import com.starwars.api.services.PlanetaService;

@Service
public class PlanetaServiceImpl implements PlanetaService {

	@Autowired
	private PlanetaRepository planetaRespository;

	private static final String STARWARS_API = "https://swapi.co/api/planets/?search=";
	// https://swapi.co/api/planets/?search=Tatooine

	private Planet accessa(String nomeDoPlaneta) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(STARWARS_API + nomeDoPlaneta);
		Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
		return request.get(Planet.class);
	}

	@Override
	public List<Planeta> listarTodos() {
		List<Planeta> planetas = this.planetaRespository.findAll();
		for (Planeta p : planetas) {
			Planet planet = accessa(p.getNome());
			p.setQtdFilmes(planet.getFilms() == null ? 0 : planet.getFilms().size());
		}
		return planetas;
	}

	@Override
	public Planeta buscarPorId(String id) {
		Planeta planeta = this.planetaRespository.findOne(id);
		if (planeta != null) {
			Planet planet = accessa(planeta.getNome());
			planeta.setQtdFilmes(planet.getFilms() == null ? 0 : planet.getFilms().size());
			return planeta;
		} else {
			return null;
		}
	}

	@Override
	public Planeta buscarPorNome(String nome) {
		Planeta planeta = this.planetaRespository.findByNome(nome);
		if (planeta != null) {
			Planet planet = accessa(planeta.getNome());
			planeta.setQtdFilmes(planet.getFilms() == null ? 0 : planet.getFilms().size());
			return planeta;
		} else {
			return null;
		}
	}

	@Override
	public Planeta cadastrar(Planeta planeta) {
		return this.planetaRespository.save(planeta);
	}

	@Override
	public Planeta atualizar(Planeta planeta) {
		return this.planetaRespository.save(planeta);
	}

	@Override
	public void remover(String id) {
		this.planetaRespository.delete(id);
	}

}
