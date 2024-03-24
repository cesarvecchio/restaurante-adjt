package br.com.restauranteadjt.performance;

import br.com.restauranteadjt.utils.Helper;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReservaPerformanceSimulation extends Simulation {
    @Override
    public void before() {

        super.before();
    }

    private final HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080")
            .header("Content-Type", "application/json");

    ActionBuilder adicionarReservaRequest = http("adicionar reserva")
            .post("/reservas/65ff1cfc171f15442bed5562")
            .body(ElFileBody("bodies/requestReserva.json"))
            .check(status().is(201));

    ScenarioBuilder cenarioAdicionarReserva = scenario("Adicionar Reserva")
            .exec(session ->
                    session.set("dataReserva", LocalDate.now()
                            .plusDays(ThreadLocalRandom.current().nextInt(1, 10000 + 1))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            )
            .exec(session ->
                    session.set("email", Helper.criarTexto() + "albert@gmail.com")
            )
            .exec(adicionarReservaRequest);

    {
        setUp(
                cenarioAdicionarReserva.injectOpen(
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
                        global().responseTime().max().lt(600),
                        global().failedRequests().count().is(0L)
                );
    }
}
