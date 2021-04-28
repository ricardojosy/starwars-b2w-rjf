package com.starwars.api.documents;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Planeta {

	@Id
	private String id;

	@NotEmpty(message = "Nome não pode ser vazio")
//	@Indexed(unique=true, sparse=true)
	private String nome;
	
	@NotEmpty(message = "Clima não pode ser vazio")
	private String clima;
	
	@NotEmpty(message = "Terreno não pode ser vazio")
	private String terreno;
	
	private Integer qtdFilmes;

	public Planeta() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClima() {
		return clima;
	}

	public void setClima(String clima) {
		this.clima = clima;
	}

	public String getTerreno() {
		return terreno;
	}

	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}

	public Integer getQtdFilmes() {
		return qtdFilmes;
	}

	public void setQtdFilmes(Integer qtdFilmes) {
		this.qtdFilmes = qtdFilmes;
	}

	@Override
	public String toString() {
		return "Planeta [id=" + id + ", nome=" + nome + ", clima=" + clima + ", terreno=" + terreno + ", qtdFilmes="
				+ qtdFilmes + "]";
	}
}
