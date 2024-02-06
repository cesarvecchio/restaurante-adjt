package br.com.restauranteadjt.infrastructure.controllers.dto.response;

import java.time.LocalTime;
import java.util.List;

public record CreateRestauranteResponse(
        String nome,
        String localizacao,
        String tipoCozinha,
        List<LocalTime> horariosFuncionamento,
        Integer capacidade
) {
}
