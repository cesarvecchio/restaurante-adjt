package br.com.restauranteadjt.bdd;

import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
import br.com.restauranteadjt.utils.Helper;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;

public class StepDefinitionReserva {
    private Response response;

    @Quando("criar uma nova reserva")
    public void criar_uma_nova_reserva() {
        var reservaRequest = criarRequisicao();

        String ENDPOINT_API_RESERVA = "http://localhost:8080/reservas";
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservaRequest)
                .when()
                .post(ENDPOINT_API_RESERVA + "/{idRestaurante}", Helper.criarIdRestaurante());
    }
    @Então("a reserva deve ser criada com sucesso")
    public void a_reserva_deve_ser_criada_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.CREATED.value());
    }
    @Então("deve ser apresentada")
    public void deve_ser_apresentada() {
        response.then()
                .extract().as(ReservaResponse.class);
    }


    private CreateReservaRequest criarRequisicao(){
        return new CreateReservaRequest(
                Helper.criarDataReserva(),
                Helper.criarHoraReserva(),
                "Albert",
                "albert@gmail.com",
                "40028922"
        );
    }
}
