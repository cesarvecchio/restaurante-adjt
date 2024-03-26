package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.AvaliacaoRequest;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.AvaliacaoVO;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AvaliacaoControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        popularCollectionResturante();
        popularCollectionReserva();
    }

    private void popularCollectionResturante() {
        AvaliacaoVO avaliacao1 = new AvaliacaoVO(PontuacaoEnum.PONTOS_2, "Comida razoável");
        AvaliacaoVO avaliacao2 = new AvaliacaoVO(PontuacaoEnum.PONTOS_3, "Comida boa");
        AvaliacaoVO avaliacao3 = new AvaliacaoVO(PontuacaoEnum.PONTOS_5, "Comida maravilhosa");

        // Criando uma lista e adicionando os objetos AvaliacaoVO
        List<AvaliacaoVO> listaAvaliacoes = new ArrayList<>();
        listaAvaliacoes.add(avaliacao1);
        listaAvaliacoes.add(avaliacao2);
        listaAvaliacoes.add(avaliacao3);


        List<RestauranteCollection> restaurantes = List.of(new RestauranteCollection("65efa722ed1aa9f4356dca85", "Burger King",
                        "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4, listaAvaliacoes),
                new RestauranteCollection("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão",
                        "Av. Raimundo Pereira de Magalhães, 1465", "Frutos do Mar", List.of(LocalTime.parse("12:00:00")), 10, null),
                new RestauranteCollection("65f30f8bb665b3d1aa9aeee3", "Pizza Hut",
                        "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null)
        );
        restauranteRepository.saveAll(restaurantes);
    }

    private void popularCollectionReserva(){
       List<ReservaCollection> reservas = List.of(
                    new ReservaCollection("65f45ec843af698ea2473e29", LocalDate.now().plusDays(2),
                            LocalTime.parse("22:00:00"), "João", "joao09@gmail.com", "11912345678",
                            new RestauranteVO("65efa722ed1aa9f4356dca85", "Burger King"), StatusMesa.OCUPADA),
                    new ReservaCollection("65f46030c68c5152198b3768", LocalDate.now().plusDays(3),
                            LocalTime.parse("12:00:00"), "Kleber", "kleber12@gmail.com", "11956781234",
                            new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.OCUPADA),
                    new ReservaCollection("65f46226e747b50d9e531998", LocalDate.now().minusDays(2),
                            LocalTime.parse("22:00:00"), "Ana", "ana@gmail.com", "11912349999",
                            new RestauranteVO("65efa722ed1aa9f4356dca85", "Burger King"), StatusMesa.FINALIZADA),
                    new ReservaCollection("65f461dc2cfffede8313b611", LocalDate.now().minusDays(3),
                            LocalTime.parse("12:00:00"), "Márcia", "marcia@gmail.com", "11999995678",
                            new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA),
                    new ReservaCollection("65f4717bd6a804d8491361fd", LocalDate.now().minusDays(4),
                            LocalTime.parse("12:00:00"), "Rodolfo", "rodolfo@gmail.com", "11989895678",
                            new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA)
            );

            reservaRepository.saveAll(reservas);
        }

    @Nested
    class CriarAvaliacao{

        @Test
        void devePermitirCriarAvaliacaoRestaurante(){
            AvaliacaoRequest avaliacaoRequest = new AvaliacaoRequest(PontuacaoEnum.PONTOS_4,
                    "Comida muito boa, porém demora bastante");
            String idReserva = "65f46226e747b50d9e531998";
            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoRequest)
                    .when()
                    .post("/avaliacoes/{idReserva}", idReserva)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/avaliacao.schema.json"));
        }

        @Test
        void deveGerarExcecao_CriarAvaliacaoRestaurante_PayloadXml(){
            String xmlPayload = "<AvaliacaoRequest><pontuacao>PontuacaoEnum.PONTOS_4</pontuacao>"+
                    "<comentario>Comida muito boa, porém demora bastante</comentario></AvaliacaoRequest>";
            String idReserva = "65f45ec843af698ea2473e29";
            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(xmlPayload)
            .when()
                    .post("/avaliacoes/{idReserva}", idReserva)
            .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Bad Request"))
                    .body("path", equalTo("/avaliacoes/" + idReserva));
        }

        @Test
        void deveGerarExcecao_Quando_CriarAvaliacaoRestaurante_IdReservaNaoExiste(){
            AvaliacaoRequest avaliacaoRequest = new AvaliacaoRequest(PontuacaoEnum.PONTOS_4,
                    "Comida muito boa, porém demora bastante");
            String idReserva = "3213321312321321";

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoRequest)
            .when()
                    .post("/avaliacoes/{idReserva}", idReserva)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Nao Encontrado Exception"))
                    .body("message", equalTo(String.format("Reserva com id:'%s' não foi encontrada", idReserva)))
                    .body("path", equalTo("/avaliacoes/" + idReserva));
        }

        @Test
        void deveGerarExcecao_Quando_CriarAvaliacaoRestaurante_MesaNaoEstaComStatusFinalizada(){
            AvaliacaoRequest avaliacaoRequest = new AvaliacaoRequest(PontuacaoEnum.PONTOS_4,
                    "Comida muito boa, porém demora bastante");
            String idReserva = "65f45ec843af698ea2473e29";

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoRequest)
                    .when()
                    .post("/avaliacoes/{idReserva}", idReserva)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Status Reserva!"))
                    .body("message", equalTo(String.format("A Reserva com id:'%s' possui o status:'%s', " +
                                    "só é possivel avaliar o resturante quando o status estiver como:'%s'",
                            idReserva, StatusMesa.OCUPADA, StatusMesa.FINALIZADA)))
                    .body("path", equalTo("/avaliacoes/" + idReserva));
        }


    }

    @Nested
    class ListarAvaliacoes{
       @Test
       void devePermitirBuscarAvaliacoesRestaurante(){
            String idRestaurante = "65efa722ed1aa9f4356dca85";

           given()
                   .filter(new AllureRestAssured())
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .body(idRestaurante)
           .when()
                   .get("/avaliacoes/{idRestaurante}", idRestaurante)
           .then()
                   .log().all()
                   .statusCode(HttpStatus.OK.value())
                   .body(matchesJsonSchemaInClasspath("schemas/avaliacao-list.schema.json"));
        }

        @Test
        void deveGerarExcecao_Quando_BuscarAvaliacoesRestaurante_NaoExisteNenhumaAvaliacao(){
            String idRestaurante = "65f30ee66ef25c44b8d9faac";
            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(idRestaurante)
                    .when()
                    .get("/avaliacoes/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Status Reserva!"))
                    .body("message", equalTo(String.format(
                            "O Restaurante com id:'%s' não possui nenhuma avaliação até o momento",
                            idRestaurante)))
                    .body("path", equalTo("/avaliacoes/" + idRestaurante));
        }
    }
}
