package br.testeapi.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;


public class VerbosTest {


	@Test
	public void deveSalvarUsuário() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\":\"Jose\",\"age\":50}")
		.when()
		.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Jose"))
		.body("age", is(50))
		;
	}

	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"age\":50}")
		.when()
		.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(400)
		.body("id", is(nullValue()))
		.body("error", is("Name é um atributo obrigatório"))

		;
	}

	@Test
	public void deveSalvarUsuárioXML() {
		given()
		.log().all()
		.contentType(ContentType.XML)
		.body("<user><name>Jose</name><age>50</age></user>")
		.when()
		.post("http://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(201)
		.body("users.@id", is(notNullValue()))
		.body("users.name", is("Jose"))
		.body("users.age", is("50"))
		;
	}

	@Test
	public void deveAlterarUsuário() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\":\"Nome Alterado\",\"age\":80}")
		.when()
		.put("http://restapi.wcaquino.me/users/1")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("Nome Alterado"))
		.body("age", is(80))
		;
	}

	@Test
	public void devoParametrizarURL() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\":\"Nome Alterado\",\"age\":80}")
		.when()
		.put("http://restapi.wcaquino.me/{entidade}/{userId}", "users", "2")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(2))
		.body("name", is("Nome Alterado"))
		.body("age", is(80))
		;
	}

	@Test
	public void devoParametrizarURL2() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\":\"Nome Alterado\",\"age\":80}")
		.pathParam("entidade", "users")
		.pathParam("userId", "2")
		.when()
		.put("http://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(2))
		.body("name", is("Nome Alterado"))
		.body("age", is(80))
		;
	}


	@Test
	public void deveExtraindoId() {
		Object id = given()
				.log().all()
				.contentType("application/json")
				.body("{\"name\":\"Jose\",\"age\":50}")
				.when()
				.post("http://restapi.wcaquino.me/users")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", is(notNullValue()))
				.body("name", is("Jose"))
				.body("age", is(50))
				.extract().path("id")
				;
		System.out.println(id);
	}

	@Test
	public void deveExcluirUsuario() {
		given()
		.log().all()
		.when()
		.delete("http://restapi.wcaquino.me/users/1")
		.then()
		.log().all()
		.statusCode(204)
		;
	}

	@Test
	public void naoDeveExcluirUsuarioInexistente() {
		given()
		.log().all()
		.when()
		.delete("http://restapi.wcaquino.me/users/100")
		.then()
		.log().all()
		.statusCode(400)
		.body("error", is("Registro inexistente"))
		;
	}

	@Test
	public void deveSalvarUsuárioUsandoMap() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("name", "Joaquin");
		parametros.put("age", 60);
		given()
		.log().all()
		.contentType("application/json")
		.body(parametros)
		.when()
		.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Joaquin"))
		.body("age", is(60))
		;
	}

	@Test
	public void deveSalvarUsuárioUsandoJsonObjeto() {
		User parametros = new User("Joaquin", 60);
		given()
		.log().all()
		.contentType("application/json")
		.body(parametros)
		.when()
		.post("http://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Joaquin"))
		.body("age", is(60))
		;
	}
	

	@Test
	public void deveDeseriallizarUsuárioUsandoJsonObjeto() {
		User parametros = new User("Joaquin", 60);

		User usuarioCriado = given()
				.log().all()
				.contentType("application/json")
				.body(parametros)
				.when()
				.post("http://restapi.wcaquino.me/users")
				.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class)
				;
		Assert.assertThat(usuarioCriado.getId(), is(notNullValue()));
		Assert.assertThat(usuarioCriado.getName(), is("Joaquin"));
		Assert.assertThat(usuarioCriado.getAge(),is(60));
	}

	@Test
	public void deveSalvarUsuárioUsandoXMLObjeto() {
		User user = new User("Usuario XML", 35);
		given()
		.log().all()
		.contentType(ContentType.XML)
		.body(user)
		.when()
		.post("http://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", is("Usuario XML"))
		.body("user.age", is("35"))
		;
	}
	
	@Test
	public void deveDeserializaraoSalvarUsuárioUsandoXMLObjeto() {
		User user = new User("Usuario XML", 35);
		User usuarioXML = given()
		.log().all()
		.contentType(ContentType.XML)
		.body(user)
		.when()
		.post("http://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class)
		;
		Assert.assertThat(usuarioXML.getId(), is(notNullValue()));
		Assert.assertThat(usuarioXML.getName(), is("Usuario XML"));
		Assert.assertThat(usuarioXML.getAge(), is(35));
	}
	
}
