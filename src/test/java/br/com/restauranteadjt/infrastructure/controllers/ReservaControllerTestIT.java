package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservaControllerTestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        popularCollectionRestauranteEReserva();
    }

    void popularCollectionRestauranteEReserva() {
        List<RestauranteCollection> restaurantes = getRestauranteCollections();
        restauranteRepository.saveAll(restaurantes);

        List<ReservaCollection> reservas = getReservas();
        reservaRepository.saveAll(reservas);
    }

    private static List<RestauranteCollection> getRestauranteCollections() {
        return List.of(new RestauranteCollection("65efa722ed1aa9f4356dca85", "Burger King",
                        "Av. Paulista", "Fast Food", List.of(LocalTime.parse("22:00:00")), 4, null),
                new RestauranteCollection("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão",
                        "Av. Raimundo Pereira de Magalhães, 1465", "Frutos do Mar", List.of(LocalTime.parse("12:00:00")), 10, null),
                new RestauranteCollection("65f30f8bb665b3d1aa9aeee3", "Pizza Hut",
                        "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null),
                new RestauranteCollection("65f532a4c76eed86ae85ee3f", "Agua Doce",
                        "Granja Viana", "Pub", List.of(LocalTime.parse("19:00:00")), 1, null)
        );
    }

    private static List<ReservaCollection> getReservas() {
        return List.of(
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
                        new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA),
                new ReservaCollection("65f4717bd6a804d8491361fd", LocalDate.now().plusDays(7),
                        LocalTime.parse("20:00:00"), "Anni Carolini", "anni@gmail.com", "11956289657",
                        new RestauranteVO("65f532a4c76eed86ae85ee3f", "Agua Doce"), StatusMesa.OCUPADA)
        );
    }

    @AfterEach
    void dropCollectionRestauranteEReserva() {
        restauranteRepository.deleteAll();
        reservaRepository.deleteAll();
    }

    @Nested
    class CriarReserva {

        @Test
        void devePermitirCriarReserva() {
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";
            LocalDate data = LocalDate.of(2024, 6, 15);
            LocalTime horario = LocalTime.parse("18:00:00");

            CreateReservaRequest createReservaRequest = new CreateReservaRequest(data, horario, "Guilherme Matos de Carvalho",
                    "guilherme@gmail.com", "11981281123");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(createReservaRequest)
                    .post("/reservas/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/reserva.schema.json"));;
        }

    }

    @Nested
    class validateExceptions {
        @Test
        void deveGerarExcecao_CriarReserva_PayloadXml(){
            String xmlPayload = "<CreateReservaRequest>" +
                    "<dataReserva>2024-03-15</dataReserva>" +
                    "<horaReserva>12:00:00</horaReserva>" +
                    "<nome>João</nome>" +
                    "<email>joao@example.com</email>" +
                    "<telefone>123456789</telefone>" +
                    "</CreateReservaRequest>";
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(xmlPayload)
                    .post("/reservas/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Bad Request"))
                    .body("path", equalTo("/reservas/" + idRestaurante));
        }
        @Test
        void deveGerarExececao_SeHorarioDaReserva_ForAnteriorADataAtual() {
            String idRestaurante = "65efa722ed1aa9f4356dca85";
            LocalDate data = LocalDate.of(2019, 6, 15);
            LocalTime horario = LocalTime.parse("22:00:00");

            CreateReservaRequest createReservaRequest = new CreateReservaRequest(data, horario, "Guilherme Matos de Carvalho",
                    "guilherme@gmail.com", "11981281123");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(createReservaRequest)
                    .post("/reservas/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Data Invalida!"))
                    .body("message", equalTo("A data e hora de reserva não pode ser anterior a data e hora atual"))
                    .body("path", equalTo("/reservas/" + idRestaurante));
        }

        @Test
        void deveGerarExececao_SeHorarioDaReserva_JaExiste() {
            String idRestaurante = "65f532a4c76eed86ae85ee3f";
            LocalDate data = LocalDate.now().plusDays(2);
            LocalTime horario = LocalTime.parse("20:00:00");

            CreateReservaRequest createReservaRequest = new CreateReservaRequest(data, horario, "Guilherme Matos de Carvalho",
                    "guilherme@gmail.com", "11981281123");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(createReservaRequest)
                    .post("/reservas/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Reserva!"))
                    .body("message", equalTo(String.format("Restaurante com id:'%s' não possuio o horario:'%s' para reserva",
                            idRestaurante, horario)))
                    .body("path", equalTo("/reservas/" + idRestaurante));
        }

        @Test
        void deveGerarExececao_SeHorarioEhDataDaReserva_JaExiste() {
            String idRestaurante = "65f532a4c76eed86ae85ee3f";
            LocalDate data = LocalDate.now().plusDays(7);
            LocalTime horario = LocalTime.parse("20:00:00");

            CreateReservaRequest createReservaRequest = new CreateReservaRequest(data, horario, "Guilherme Matos de Carvalho",
                    "guilherme@gmail.com", "11981281123");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(createReservaRequest)
                    .post("/reservas/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Reserva!"))
                    .body("message", equalTo(String.format(
                            "O Restaurante com id:'%s' não possui reserva livre para a data:'%s' e hora:'%s'",
                            idRestaurante, data, horario)))
                    .body("path", equalTo("/reservas/" + idRestaurante));
        }

        @Test
        void deveGerarExececao_SeJaPossuiReserva_NaDataEhHorario() {
            String idRestaurante = "65efa722ed1aa9f4356dca85";
            LocalDate data =  LocalDate.now().plusDays(2);
            LocalTime horario = LocalTime.parse("22:00:00");

            CreateReservaRequest createReservaRequest = new CreateReservaRequest(data, horario, "João",
                    "joao09@gmail.com", "11912345678");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(createReservaRequest)
                    .post("/reservas/{idRestaurante}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Já Possui Reserva!"))
                    .body("message", equalTo(String.format(
                            "Você já possui uma reserva agendada no restaurante com id:'%s' para a data:'%s' e hora:'%s'",
                            idRestaurante, data, horario)))
                    .body("path", equalTo("/reservas/" + idRestaurante));
        }
    }
}
