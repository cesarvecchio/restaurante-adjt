# language: pt
Funcionalidade:

  Cenario:
    Dado que uma reserva foi efetuada e finalizada
    Então deve registrar avaliacao
    E retornar a avaliacao com sucesso

  Cenario:
    Dado que restaurante possui avaliacoes
    Quando realizar a busca das avaliacoes
    Então as avaliacoes devem ser apresentadas com sucesso