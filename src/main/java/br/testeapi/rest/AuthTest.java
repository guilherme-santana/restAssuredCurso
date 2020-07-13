package br.testeapi.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class AuthTest {
	
	@Test
	public void deveAcessarSwapi() {
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void deveObterClimaComChave() {
		given()
			.log().all()
			.queryParam("q", "Fortaleza,BR")
			.queryParam("appid", "e28ab638cc0be7c8b908e33ae98f68e8")
			.queryParam("units", "metric")
		.when()
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Fortaleza"))
		;
	}
	
	@Test
	public void naoDeveAcessar() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() {
		given()
			.log().all()
		.when()
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "santanaguilherme@teste.com");
		login.put("senha", "123456");
		
		//receberToken
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
		
		//obterContas
		given()
			.log().all()
			.header("Authorization", "JWT "+token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta de teste"))
		;
		
		
	}

}
