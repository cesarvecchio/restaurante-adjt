package br.com.restauranteadjt.performance;

import br.com.restauranteadjt.domain.enums.PontuacaoEnum;
import br.com.restauranteadjt.utils.Helper;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AvaliacaoPerformanceSimulation extends Simulation {

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
            .check(jsonPath("$.id").saveAs("idReserva"));

    ActionBuilder atualizarStatusMesaRequest = http("request: atualizar status mesa")
            .put("/mesas/#{idReserva}")
            .body(StringBody("{\"statusMesa\": \"FINALIZADA\"}"))
            .check(status().is(202));

    ActionBuilder adicionarAvaliacaoRestauranteRequest = http("request: adicionar avaliação restaurante")
            .post("/avaliacoes/#{idReserva}")
            .body(ElFileBody("bodies/requestAvaliacao.json"))
            .check(status().is(201));

    ActionBuilder listarAvaliacoesRestauranteRequest = http("request: listar avaliações restaurante")
            .get("/avaliacoes/#{idRestaurante}")
            .check(status().is(200));

    ScenarioBuilder cenarioAdicionarAvaliacaoRestaurante = scenario("Adicionar avaliação restaurante")
            .exec(session -> session.set("nome", "Restaurante" + getRandomNumber()))
            .exec(adicionarRestauranteRequest)
            .exec(session ->
                    session.set("dataReserva", LocalDate.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            )
            .exec(session ->
                    session.set("email", Helper.criarTexto() + "albert@gmail.com")
            )
            .exec(adicionarReservaRequest)
            .exec(atualizarStatusMesaRequest)
            .exec(session -> session.set("pontuacao", getPontuacaoAleatoria()))
            .exec(adicionarAvaliacaoRestauranteRequest);

    ScenarioBuilder cenarioListarAvaliacoesRestaurante = scenario("Listar avaliações restaurante")
            .exec(session -> session.set("nome", "Restaurante" + getRandomNumber()))
            .exec(adicionarRestauranteRequest)
            .exec(session ->
                    session.set("dataReserva", LocalDate.now()
                            .plusDays(ThreadLocalRandom.current().nextInt(1, 10000 + 1))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            )
            .exec(session ->
                    session.set("email", Helper.criarTexto() + "albert@gmail.com")
            )
            .exec(adicionarReservaRequest)
            .exec(atualizarStatusMesaRequest)
            .exec(session -> session.set("pontuacao", getPontuacaoAleatoria()))
            .exec(adicionarAvaliacaoRestauranteRequest)
            .exec(listarAvaliacoesRestauranteRequest);

    {
        setUp(
                cenarioAdicionarAvaliacaoRestaurante.injectOpen(
                        rampUsersPerSec(1)
                                .to(2)
                                .during(Duration.ofSeconds(10)),
                        constantUsersPerSec(10)
                                .during(Duration.ofSeconds(20)),
                        rampUsersPerSec(2)
                                .to(1)
                                .during(Duration.ofSeconds(10))
                ),
                cenarioListarAvaliacoesRestaurante.injectOpen(
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

    private String getPontuacaoAleatoria() {
        List<PontuacaoEnum> pontuacoes = List.of(
                PontuacaoEnum.PONTO_1,
                PontuacaoEnum.PONTOS_2,
                PontuacaoEnum.PONTOS_3,
                PontuacaoEnum.PONTOS_4,
                PontuacaoEnum.PONTOS_5
        );
        int index = random.nextInt(5);
        return pontuacoes.get(index).toString();
    }
}
