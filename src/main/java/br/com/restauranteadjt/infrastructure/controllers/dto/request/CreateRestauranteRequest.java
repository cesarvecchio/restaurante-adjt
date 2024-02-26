package br.com.restauranteadjt.infrastructure.controllers.dto.request;

import br.com.restauranteadjt.domain.valueObject.Endereco;

import java.time.LocalTime;
import java.util.List;

public record CreateRestauranteRequest(
        String nome,
        Endereco localizacao,
        String tipoCozinha,
        List<LocalTime> horariosFuncionamento,
        Integer capacidade
) {
}
