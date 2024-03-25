build-project:
	gradle build --refresh-dependencies -x test

unit-test: gradle-clean
	gradle unitTest

integration-test: gradle-clean
	gradle integrationTest

system-test:
	gradle cucumberCli

performance-test: gradle-clean
	gradle gatlingRun

performance-restaurante: gradle-clean
	gradle gatlingRun-br.com.restauranteadjt.performance.RestaurantePerformanceSimulation

performance-reserva: gradle-clean
	gradle gatlingRun-br.com.restauranteadjt.performance.ReservaPerformanceSimulation

performance-mesa: gradle-clean
	gradle gatlingRun-br.com.restauranteadjt.performance.MesaPerformanceSimulation

performance-avaliacao: gradle-clean
	gradle gatlingRun-br.com.restauranteadjt.performance.AvaliacaoPerformanceSimulation

test: unit-test integration-test

gradle-clean:
	gradle clean

docker-build:
	docker image prune --force --filter 'label=backend-dev'
	docker build -t backend:dev --label backend-dev .

docker-start:
	docker compose -f docker-compose.yaml up -d

docker-stop:
	docker compose -f docker-compose.yaml down