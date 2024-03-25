## Execução dos Testes
**Requisitos para executar os comandos a seguir:**
 - Ter acesso ao comando make (Necessário para execução de todos os comandos a seguir);
 - Ter o docker instalado (Necessário para execução de comandos relacionados ao docker);
 - Ter o Gradle instalado (Necessário para execução de todos os comandos a seguir);

### Para executar os testes unitários:
```sh
make unit-test
```

### Para executar os testes integrados:
```sh
make integration-test
```

### Para executar os testes unitários + testes integrados:
```sh
make test
```

### Para executar os testes de sistema/comportamento:
O fluxo recomendado para esse teste é de 
iniciar 2 contêineres docker, um executando a aplicação 
e outro executando um banco de dados mongodb, 
com a finalidade de não sujar a sua base de 
dados do mongodb instalado localmente em sua máquina.<br>
Para isso vamos seguir essas 5 etapas:<br>
#### 1) Realizar o build do projeto:
```sh
make build-project
```
#### 2) Realizar o build da imagem da aplicação:
```sh
make docker-build
```
#### 3) Subir os contêineres:
```sh
make docker-start
```
#### 4) Executar os testes de sistema/comportamento:
```sh
make system-test
```
#### 5) Remover os contêineres e recursos que foram criados junto com eles:
```sh
make docker-stop
```

### Para executar os testes de performance:
O fluxo recomendado para os testes de performance seguem 
na mesma linha dos testes de sistema,
iniciar 2 contêineres docker, um executando a aplicação
e outro executando um banco de dados mongodb.<br>
Para isso vamos seguir essas 8 etapas:<br>
#### 1) Realizar o build do projeto:
```sh
make build-project
```
#### 2) Realizar o build da imagem da aplicação:
```sh
make docker-build
```
#### 3) Subir os contêineres:
```sh
make docker-start
```
#### 4) Executar os testes de performance de restaurante :
```sh
make performance-restaurante
```
#### 5) Executar os testes de performance de reserva :
```sh
make performance-reserva
```
#### 6) Executar os testes de performance de mesa :
```sh
make performance-mesa
```
#### 7) Executar os testes de performance de avaliacao :
```sh
make performance-avaliacao
```
#### Obs.: Caso queira executar todos os testes de performance utilize o seguinte comando:
```sh
make performance-test
```
#### 8) Remover os contêineres e recursos que foram criados junto com eles:
```sh
make docker-stop
```