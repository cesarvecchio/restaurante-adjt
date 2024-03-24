# language: pt
Funcionalidade: Mesa

  Cenario: Buscar Mesa
    Dado que uma reserva foi efetuada
    Quando realizar a busca da mesa
    Então a mesa deve ser apresentado com sucesso

  Cenario: Atualizar Status da Mesa
    Dado que uma reserva foi efetuada e atendida
    Então deve atualizar status da mesa
    E retornar a mesa com status atualizado com sucesso
