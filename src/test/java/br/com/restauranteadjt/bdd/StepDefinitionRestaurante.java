package br.com.restauranteadjt.bdd;

import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import br.com.restauranteadjt.utils.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class StepDefinitionRestaurante {
    private Response response;
    private RestauranteResponse restauranteResponse;
    private List<RestauranteResponse> restauranteResponseList;
    private final String ENDPOINT_API_RESTAURANTE = "http://localhost:8080/restaurantes";

    @Quando("criar um novo restaurante")
    public RestauranteResponse criar_um_novo_restaurante() {
        var restauranteRequest = criarRequisicao();

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteRequest)
                .when()
                .post(ENDPOINT_API_RESTAURANTE);

        return response.then().extract().as(RestauranteResponse.class);
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

    @Dado("que um restaurante foi criado")
    public void que_um_restaurante_foi_criado() {
        restauranteResponse = criar_um_novo_restaurante();
    }

    @Quando("realizar a busca")
    @SuppressWarnings("unchecked")
    public void realizar_a_busca() {
        response = when()
                .get(ENDPOINT_API_RESTAURANTE +
                        "?nome=" + restauranteResponse.nome() +
                        "&tipoCozinha=" + restauranteResponse.tipoCozinha() +
                        "&endereco=" + restauranteResponse.localizacao());

        restauranteResponseList = new ArrayList<>(response.then().extract()
                .as(new TypeReference<List<RestauranteResponse>>() {
                }.getType()));
    }

    @Então("o restaurante deve ser apresentado com sucesso")
    public void o_restaurante_deve_ser_apresentado_com_sucesso() {
        response.then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo(restauranteResponseList.get(0).id()))
                .body("[0].nome", equalTo(restauranteResponseList.get(0).nome()))
                .body("[0].tipoCozinha", equalTo(restauranteResponseList.get(0).tipoCozinha()))
                .body("[0].localizacao", equalTo(restauranteResponseList.get(0).localizacao()));
    }


    private CreateRestauranteRequest criarRequisicao() {
        return new CreateRestauranteRequest(
                null,
                "Restaurante" + Helper.criarTexto(),
                "Localizacao",
                "Tipo cozinha",
                List.of(Helper.criarHoraReserva()),
                1
        );
    }
}
