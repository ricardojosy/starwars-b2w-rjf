package com.starwars.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.starwars.api.documents.Planeta;
import com.starwars.api.responses.Response;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetaApplicationTests {

	private final String URL_BASE = "http://localhost:8080/api/planetas/";

	@Test
	public void dadoUmNomeNaoExistente_entaoRetorna404() throws ClientProtocolException, IOException {
		String nome = "xpto";
		HttpUriRequest request = new HttpGet(URL_BASE + "por-nome?nome=" + nome);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
	}

	@Test
	public void buscandoPeloNome() throws ClientProtocolException, IOException {
		String nome = "MeuPlaneta";
		HttpUriRequest httpRequest = new HttpGet(URL_BASE + "por-nome?nome=" + nome);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpRequest);

		String json = EntityUtils.toString(httpResponse.getEntity());
		Gson gson = new Gson();
		Response<?> response = gson.fromJson(json, Response.class);
		Planeta planeta = gson.fromJson((String) response.getData().toString(), Planeta.class);

		assertNotNull(planeta);
		assertEquals("5c75a58f44493b2498f019d4", planeta.getId());
	}

	@Test
	public void buscandoPeloId() throws ClientProtocolException, IOException {
		String id = "5c75a58f44493b2498f019d4";
		HttpUriRequest httpRequest = new HttpGet(URL_BASE + id);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpRequest);
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	}

	@Test
	public void dadoUmIdNaoExistente_entaoRetorna404() throws ClientProtocolException, IOException {
		String id = "xpto";
		HttpUriRequest request = new HttpGet(URL_BASE + id);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
	}

	@Test
	public void cadastrandoUmPlaneta() throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"nome\": ");
		sb.append("\"MeuPlaneta" + Math.random() + "\", ");
//		sb.append("\"MeuPlaneta" + "\", ");
		sb.append("\"clima\": \"tropical\",\"terreno\": \"acidentado\"}");
		HttpPost request = new HttpPost(URL_BASE);
		StringEntity params = new StringEntity(sb.toString());
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	}
	
	@Test
	public void alterandoUmPlaneta() throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\": \"5c77f7b944493b28d66a7b1c\", \"nome\": \"MeuPlaneta6\", \"clima\": \"tropical\", \"terreno\": \"acidentado\"}");
		String id = "5c77f7b944493b28d66a7b1c";
		HttpPut request = new HttpPut(URL_BASE + id);
		StringEntity params = new StringEntity(sb.toString());
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	}

	@Test
	public void dadoUmIdNaoExistenteParaAlterar_entaoRetorna404() throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\": \"5c77f7b944493b28d66a7b1c\", \"nome\": \"MeuPlaneta6\", \"clima\": \"tropical\", \"terreno\": \"acidentado\"}");
		String id = "xpto";
		HttpPut request = new HttpPut(URL_BASE + id);
		StringEntity params = new StringEntity(sb.toString());
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
	}

	@Test
	public void excluindoUmPlaneta() throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"nome\": \"MeuPlaneta4\", \"clima\": \"tropical\", \"terreno\": \"acidentado\"}");
		String id = "5c78530f44493b2ae30c09ba";
		HttpDelete request = new HttpDelete(URL_BASE + id);
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
	}

	@Test
	public void dadoUmIdNaoExistenteParaExcluir_entaoRetorna404() throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"nome\": \"MeuPlaneta4\", \"clima\": \"tropical\", \"terreno\": \"acidentado\"}");
		String id = "xpto";
		HttpDelete request = new HttpDelete(URL_BASE + id);
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));
	}

}
