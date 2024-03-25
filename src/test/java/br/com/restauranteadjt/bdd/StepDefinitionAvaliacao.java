package br.com.restauranteadjt.bdd;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.AvaliacaoRequest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class StepDefinitionAvaliacao {
    private Response response;
    private final String ENDPOINT_API_MESA = "http://localhost:8080/avaliacoes";
    private MesaResponse mesa;
    protected StepDefinitionMesa stepMesa;


    @Dado("que uma reserva foi efetuada e finalizada")
    public void que_uma_reserva_foi_efetuada_e_finalizada() {
        stepMesa = new StepDefinitionMesa();
        stepMesa.que_uma_reserva_foi_efetuada_e_atendida();
        mesa = stepMesa.deve_atualizar_status_da_mesa();
    }
    @Então("deve registrar avaliacao")
    public void deve_registrar_avaliacao() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new AvaliacaoRequest(PontuacaoEnum.PONTOS_5, "Otimo restaurante!!"))
                .when()
                .post(ENDPOINT_API_MESA + "/{idReserva}",
                        mesa.id());
    }
    @Então("retornar a avaliacao com sucesso")
    public void retornar_a_avaliacao_com_sucesso() {
        response.then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("pontuacao", notNullValue())
                .body("comentario", notNullValue());
    }


    @Dado("que restaurante possui avaliacoes")
    public void que_restaurante_possui_avaliacoes() {
        que_uma_reserva_foi_efetuada_e_finalizada();
        deve_registrar_avaliacao();
    }
    @Quando("realizar a busca das avaliacoes")
    public void realizar_a_busca_das_avaliacoes() {
        response = given()
                .when()
                .get(ENDPOINT_API_MESA + "/{idRestaurante}",
                        stepMesa.stepReserva.restauranteResponse.id());
    }
    @Então("as avaliacoes devem ser apresentadas com sucesso")
    public void as_avaliacoes_devem_ser_apresentadas_com_sucesso() {
    }


}
