build-project:gradle-clean
	gradle build --refresh-dependencies -x test

unit-test: gradle-clean
	gradle unitTest

integration-test: gradle-clean
	gradle integrationTest

test: unit-test integration-test

gradle-clean:
	gradle clean

docker-build:
	docker build -t backend:dev .

docker-start:
	docker compose -f docker-compose.yaml up -d

system-test:
	gradle cucumberCli

docker-stop:
	docker compose -f docker-compose.yaml down