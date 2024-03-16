package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestauranteControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        popularCollectionResturante();
    }

    private void popularCollectionResturante() {
        List<RestauranteCollection> restaurantes = List.of(new RestauranteCollection("65efa722ed1aa9f4356dca85", "Burger King",
                        "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4, null),
                new RestauranteCollection("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão",
                        "Av. Raimundo Pereira de Magalhães, 1465", "Frutos do Mar", List.of(LocalTime.parse("12:00:00")), 10, null),
                new RestauranteCollection("65f30f8bb665b3d1aa9aeee3", "Pizza Hut",
                        "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null)
        );
        restauranteRepository.saveAll(restaurantes);
    }

    @Nested
    class CriarRestaurante {

        @Test
        void devePermitirCriarRestaurante() {
            CreateRestauranteRequest restauranteRequest = new CreateRestauranteRequest(
                    "Hi Pokee",
                    "R. Augusta, 2068",
                    "Comida Havaiana",
                    List.of(LocalTime.now()),
                    10
            );

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteRequest)
            .when()
                .post("/restaurantes")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCriarRestaurante_PayloadXML() {
            String xmlPayload = "<restaurante><nome>Hi Pokee</nome><localizacao>R. Augusta, 2068</localizacao>" +
                    "<tipoCozinha>Comida Havaiana</tipoCozinha>" +
                    "<horariosFuncionamento><element>15:16:50.6189705</element></horariosFuncionamento>" +
                    "<capacidade>10</capacidade></restaurante>";

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(xmlPayload)
            .when()
                .post("/restaurantes")
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                .body("error", equalTo("Bad Request"))
                .body("path", equalTo("/restaurantes"));
        }

        @Test
        void deveGerarExcecao_Quando_CriarRestaurante_Nome_TipoCozinha_Localizacao_JaExiste() {
            String nome = "Burger King";
            String localizacao = "Av. Paulista";
            String tipoCozinha = "Fast Food";
            CreateRestauranteRequest restauranteRequest = new CreateRestauranteRequest(
                    nome,
                    localizacao,
                    tipoCozinha,
                    List.of(LocalTime.parse("22:00:00")),
                    4
            );

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteRequest)
            .when()
                .post("/restaurantes")
            .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                .body("error", equalTo("Já possui cadastro!"))
                .body("message", equalTo(String.format(
                        "Restaurante com nome:'%s', " +
                        "tipoCozinha:'%s' e localizacao:'%s' " +
                        "já está cadastrado no sistema", nome, tipoCozinha, localizacao))
                )
                .body("path", equalTo("/restaurantes"));
        }
    }

    @Nested
    class BuscarRestaurante {

        @ParameterizedTest
        @CsvSource(value={
                "Pizza Hut; ; ",
                " ;Pizzaria; ",
                " ; ;R. Carlos Weber, 344",
                "Pizza Hut;Pizzaria; ",
                "Pizza Hut; ;R. Carlos Weber, 344",
                " ;Pizzaria;R. Carlos Weber, 344",
                " ; ; ",
        }, delimiter = ';')
        void devePermitirBuscarRestaurantePorNomeEOuTipoCozinhaEOuLocalizacao_InformandoSomenteNome(String nome, String tipoCozinha, String localizacao) {
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("nome", nome)
                .queryParam("tipoCozinha", tipoCozinha)
                .queryParam("endereco", localizacao)
            .when()
                .get("/restaurantes")
            .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("schemas/restaurante-list.schema.json"));
        }
    }

}
