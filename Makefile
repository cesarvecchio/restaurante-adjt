build-project:gradle-clean
	gradle build --refresh-dependencies -x test

unit-test: gradle-clean
	gradle unitTest

integration-test: gradle-clean
	gradle integrationTest

test: unit-test integration-test

gradle-clean:
	gradle clean

docker-build: build-project
	docker build -t backend:dev .

docker-start: docker-build
	docker compose -f docker-compose.yaml up -d

system-test: docker-start
	gradle cucumberCli

docker-drop-database: system-test
	docker exec -it mongodb-restaurante bash -c "mongosh --eval 'use restaurante-teste' --eval  'db.dropDatabase()'"

docker-stop: docker-drop-database
	docker compose -f docker-compose.yaml stop