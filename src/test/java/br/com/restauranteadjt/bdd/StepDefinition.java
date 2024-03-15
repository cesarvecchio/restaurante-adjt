package br.com.restauranteadjt.bdd;

import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;

public class StepDefinition {

    private Response response;

    private RestauranteResponse restauranteResponse;
    private final String ENDPOINT_API_RESTAURANTE = "http://localhost:8080/restaurantes";

    @Quando("criar um novo restaurante")
    public void criar_um_novo_restaurante() {
        var restauranteRequest = new CreateRestauranteRequest(
                "Restaurante",
                "Localizacao",
                "Tipo cozinha",
                List.of(LocalTime.of(10, 0, 0, 0)),
                3
        );

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteRequest)
                .when()
                .post(ENDPOINT_API_RESTAURANTE);
    }
    @Então("o restaurante é criado com sucesso")
    public void o_restaurante_é_criado_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.CREATED.value());
    }
    @Então("deve ser apresentado")
    public void deve_ser_apresentado() {
        response.then()
                .body("id", notNullValue());
    }
}
