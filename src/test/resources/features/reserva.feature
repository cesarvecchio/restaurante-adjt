# language: pt
Funcionalidade: Reserva

  Cenario: Criar Reserva
    Dado que um restaurante existe e possui vaga
    Quando criar uma nova reserva
    Então a reserva deve ser criada com sucesso
    E deve ser apresentada