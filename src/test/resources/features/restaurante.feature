# language: pt
Funcionalidade: Restaurante

  Cenario: Criar Restaurante
    Quando criar um novo restaurante
    Então o restaurante é criado com sucesso
    E deve ser apresentado

  Cenario: Buscar Restaurante
    Dado que um restaurante foi criado
    Quando realizar a busca
    Então o restaurante deve ser apresentado com sucesso