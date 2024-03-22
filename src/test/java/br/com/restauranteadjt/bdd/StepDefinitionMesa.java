package br.com.restauranteadjt.bdd;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.StatusMesaRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StepDefinitionMesa {
    private Response response;
    private List<MesaResponse> mesaResponseList;
    private MesaResponse mesaResponse;
    private ReservaResponse reserva;
    private final String ENDPOINT_API_MESA = "http://localhost:8080/mesas";
    protected StepDefinitionReserva stepReserva;

    @Dado("que uma reserva foi efetuada")
    public void que_uma_reserva_foi_efetuada() {
        stepReserva = new StepDefinitionReserva();
        stepReserva.que_um_restaurante_existe_e_possui_vaga();
        reserva = stepReserva.criar_uma_nova_reserva();
    }
    @Quando("realizar a busca da mesa")
    public MesaResponse realizar_a_busca_da_mesa() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_API_MESA
                                + "/{idRestaurante}"
                                + "/{dataReserva}"
                                + "/{horaReserva}"
                                + "?statusMesa=" + StatusMesa.OCUPADA,
                        stepReserva.restauranteResponse.id(),
                        reserva.dataReserva().toString(),
                        reserva.horaReserva().toString());

        mesaResponseList = new ArrayList<>(response.then().extract()
                .as(new TypeReference<List<MesaResponse>>(){}.getType()));

        assertNotNull(mesaResponseList);

        return mesaResponseList.get(0);
    }
    @Então("a mesa deve ser apresentado com sucesso")
    public void a_mesa_deve_ser_apresentado_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value());

        mesaResponseList = new ArrayList<>(response.then().extract()
                .as(new TypeReference<List<MesaResponse>>(){}.getType()));

        assertNotNull(mesaResponseList);
    }


    @Dado("que uma reserva foi efetuada e atendida")
    public void que_uma_reserva_foi_efetuada_e_atendida() {
        que_uma_reserva_foi_efetuada();
        mesaResponse = realizar_a_busca_da_mesa();
    }
    @Então("deve atualizar status da mesa")
    public MesaResponse deve_atualizar_status_da_mesa() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new StatusMesaRequest(StatusMesa.FINALIZADA))
                .when()
                .put(ENDPOINT_API_MESA + "/{idReserva}",
                        mesaResponse.id());

        return response.then().extract().as(MesaResponse.class);
    }
    @Então("retornar a mesa com status atualizado com sucesso")
    public void retornar_a_mesa_com_status_atualizado_com_sucesso() {
        response.then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body("statusMesa", equalTo(StatusMesa.FINALIZADA.toString()));

    }
}
