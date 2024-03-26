package br.com.restauranteadjt.performance;

import br.com.restauranteadjt.utils.Helper;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MesaPerformanceSimulation extends Simulation {
    Random random = new Random();

    private final HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080")
                    .header("Content-Type", "application/json");

    ActionBuilder adicionarRestauranteRequest = http("request: adicionar restaurante")
            .post("/restaurantes")
            .body(ElFileBody("bodies/requestRestaurante.json"))
            .check(status().is(201))
            .check(jsonPath("$.id").saveAs("idRestaurante"));

    ActionBuilder adicionarReservaRequest = http("request: adicionar reserva")
            .post("/reservas/#{idRestaurante}")
            .body(ElFileBody("bodies/requestReserva.json"))
            .check(status().is(201))
            .check(jsonPath("$.id").saveAs("idReserva"))
            .check(jsonPath("$.dataReserva").saveAs("dataReserva"))
            .check(jsonPath("$.horaReserva").saveAs("horaReserva"));

    ActionBuilder listarMesas = http("request: listar mesas")
            .get("/mesas/#{idRestaurante}" +
                    "/#{dataReserva}" +
                    "/#{horaReserva}" +
                    "?statusMesa=OCUPADA")
            .check(status().is(200));

    ActionBuilder atualizarStatusMesaRequest = http("request: atualizar status mesa")
            .put("/mesas/#{idReserva}")
            .body(StringBody("{\"statusMesa\": \"FINALIZADA\"}"))
            .check(status().is(202));

    ScenarioBuilder cenarioListarMesas = scenario("Listar Mesas")
            .exec(session -> session.set("nome", "Restaurante" + Helper.criarTexto()))
            .exec(adicionarRestauranteRequest)
            .exec(session ->
                    session.set("dataReserva", LocalDate.now().plusDays(2)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            ).exec(session ->
                    session.set("nome", Helper.criarTexto())
            )
            .exec(session ->
                    session.set("email", Helper.criarTexto() + "albert@gmail.com")
            )
            .exec(adicionarReservaRequest)
            .exec(listarMesas);

    ScenarioBuilder cenarioAtualizarStatusMesa = scenario("Atualizar Status Mesa")
            .exec(session -> session.set("nome", "Restaurante" + getRandomNumber()))
            .exec(adicionarRestauranteRequest)
            .exec(session ->
                    session.set("dataReserva", LocalDate.now().plusDays(2)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            ).exec(session ->
                    session.set("nome", Helper.criarTexto())
            )
            .exec(session ->
                    session.set("email", Helper.criarTexto() + "albert@gmail.com")
            )
            .exec(adicionarReservaRequest)
            .exec(atualizarStatusMesaRequest);

    {
        setUp(
                cenarioListarMesas.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))
                ),
                cenarioAtualizarStatusMesa.injectOpen(
                        rampUsersPerSec(1)
                                .to(10)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(10)
                                .to(1)
                                .during(Duration.ofSeconds(10))
                )

        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(1200),
                        global().failedRequests().count().is(0L)
                );
    }


    private Integer getRandomNumber() {
        return random.nextInt();
    }
}