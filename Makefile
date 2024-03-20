build-project:
	gradle build --refresh-dependencies -x test

unit-test: gradle-clean
	gradle unitTest

integration-test: gradle-clean
	gradle integrationTest

system-test:
	gradle cucumberCli

test: unit-test integration-test

gradle-clean:
	gradle clean

docker-build: build-project
	docker build -t backend:dev .

docker-start:
	docker compose -f docker-compose.yaml up -d

docker-stop:
	docker compose -f docker-compose.yaml down