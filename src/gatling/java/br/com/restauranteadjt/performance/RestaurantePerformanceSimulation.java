package br.com.restauranteadjt.performance;

import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class RestaurantePerformanceSimulation extends Simulation {

    Random random = new Random();

    private final HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080")
                    .header("Content-Type", "application/json");

    ActionBuilder adicinarRestauranteRequest = http("adicionar restaurante")
            .post("/restaurantes")
            .body(ElFileBody("bodies/request.json"))
            .check(status().is(201));

    ScenarioBuilder cenarioAdicionarRestaurante = scenario("Adicionar restaurante")
            .exec(session -> session.set("nome", "Restaurante" + getRandomNumber()))
            .exec(adicinarRestauranteRequest);

    {
        setUp(
                cenarioAdicionarRestaurante.injectOpen(
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

    private Integer getRandomNumber() {
        return random.nextInt();
    }
}
