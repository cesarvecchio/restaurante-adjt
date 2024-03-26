package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.StatusMesaRequest;
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
public class MesaControllerIT {
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
                        "R. Carlos Weber, 344", "Pizzaria", List.of(LocalTime.parse("18:00:00")), 3, null)
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
                        new RestauranteVO("65f30ee66ef25c44b8d9faac", "Vivenda do Camarão"), StatusMesa.FINALIZADA)
        );
    }

    @AfterEach
    void dropCollectionRestauranteEReserva() {
        restauranteRepository.deleteAll();
        reservaRepository.deleteAll();
    }


    @Nested
    class BuscarMesas {
        @Test
        void devePermitirListarMesasPeloIdDoRestaurantes() {
            String idRestaurante = "65efa722ed1aa9f4356dca85";
            String dataReserva = LocalDate.now().plusDays(2).toString();
            String horaReserva = "22:00:00";
            StatusMesa statusMesa = StatusMesa.OCUPADA;

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .param("statusMesa", statusMesa)
                    .get("/mesas/{idRestaurante}/{dataReserva}/{horaReserva}", idRestaurante, dataReserva, horaReserva)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/mesa-list.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoNaoExiste_ReservaNoRestaurante() {
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";
            String dataReserva = "2024-03-30";
            String horaReserva = "18:00";
            StatusMesa statusMesa = StatusMesa.LIVRE;

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("statusMesa", statusMesa)
                    .when()
                    .get("/mesas/{idRestaurante}/{dataReserva}/{horaReserva}", idRestaurante, dataReserva, horaReserva)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Nao Encontrado Exception"))
                    .body("message", equalTo(String.format(
                            "O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s' com o status:'%s'",
                            idRestaurante, dataReserva, horaReserva, statusMesa)))
                    .body("path", equalTo("/mesas/" + idRestaurante + "/" + dataReserva + "/" + horaReserva.replace(":", "%3A")));

        }


        @Test
        void deveGerarExcecao_QuandoNaoExiste_ReservaNoRestaurante_QuandoStatusMesaForNull() {
            String idRestaurante = "65f30f8bb665b3d1aa9aeee3";
            String dataReserva = "2024-03-30";
            String horaReserva = "18:00";
            StatusMesa statusMesa = null;

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("statusMesa", statusMesa)
                    .when()
                    .get("/mesas/{idRestaurante}/{dataReserva}/{horaReserva}", idRestaurante, dataReserva, horaReserva)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Nao Encontrado Exception"))
                    .body("message", equalTo(String.format("O Restaurante com id:'%s' não possui nenhuma reserva para está data:'%s' e hora:'%s'",
                            idRestaurante, dataReserva, horaReserva)))
                    .body("path", equalTo("/mesas/" + idRestaurante + "/" + dataReserva + "/" + horaReserva.replace(":", "%3A")));
        }

    }

    @Nested
    class AtualizarMesa {

        @Test
        void devePermitirAtualizarMesa() {
            String idReserva = "65f45ec843af698ea2473e29";
            StatusMesaRequest statusMesaRequest = new StatusMesaRequest(StatusMesa.FINALIZADA);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(statusMesaRequest)
                    .put("/mesas/{idReserva}", idReserva)
                    .then()
                    .statusCode(HttpStatus.ACCEPTED.value())
                    .log().all()
                    .body(matchesJsonSchemaInClasspath("schemas/mesa.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoReserva_NaoForEncontrada() {
            String idReserva = "43243fdwe2qewq213231";
            StatusMesaRequest statusMesaRequest = new StatusMesaRequest(StatusMesa.FINALIZADA);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(statusMesaRequest)
                    .put("/mesas/{idReserva}", idReserva)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Nao Encontrado Exception"))
                    .body("message", equalTo(String.format("Reserva com id:'%s' não foi encontrado", idReserva)))
                    .body("path", equalTo("/mesas/" + idReserva));
        }

        @Test
        void deveGerarExcecao_QuandoReserva_JaPossuiStatus() {
            String idReserva = "65f45ec843af698ea2473e29";
            StatusMesaRequest statusMesaRequest = new StatusMesaRequest(StatusMesa.OCUPADA);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .body(statusMesaRequest)
                    .put("/mesas/{idReserva}", idReserva)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/error.schema.json"))
                    .body("error", equalTo("Status Reserva!"))
                    .body("message", equalTo(String.format("Reserva com id:'%s' já possui o status:'%s'",
                            idReserva, statusMesaRequest.getStatusMesa())))
                    .body("path", equalTo("/mesas/" + idReserva));
        }
    }
}