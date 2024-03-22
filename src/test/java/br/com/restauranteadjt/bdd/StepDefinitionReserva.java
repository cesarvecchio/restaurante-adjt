package br.com.restauranteadjt.bdd;

import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import br.com.restauranteadjt.utils.Helper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class StepDefinitionReserva {
    private Response response;
    protected RestauranteResponse restauranteResponse;

    @Dado("que um restaurante existe e possui vaga")
    public void que_um_restaurante_existe_e_possui_vaga() {
        restauranteResponse = new StepDefinitionRestaurante().criar_um_novo_restaurante();
    }

    @Quando("criar uma nova reserva")
    public ReservaResponse criar_uma_nova_reserva() {
        var reservaRequest = criarRequisicao();

        String ENDPOINT_API_RESERVA = "http://localhost:8080/reservas";
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservaRequest)
                .when()
                .post(ENDPOINT_API_RESERVA + "/{idRestaurante}", restauranteResponse.id());

        return response.then().extract().as(ReservaResponse.class);
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
                Helper.criarTexto() + "_albert@gmail.com",
                "40028922"
        );
    }
}
