package br.testeapi.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		Response request = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertEquals(200, request.getStatusCode());
				
	}
	
	@Test
	public void validarBody() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"));

	}
}
