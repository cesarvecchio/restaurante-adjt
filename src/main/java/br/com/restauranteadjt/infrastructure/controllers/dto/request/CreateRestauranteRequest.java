package br.com.restauranteadjt.infrastructure.controllers.dto.request;

import java.time.LocalTime;
import java.util.List;

public record CreateRestauranteRequest(
        String id,
        String nome,
        String localizacao,
        String tipoCozinha,
        List<LocalTime> horariosFuncionamento,
        Integer capacidade
) {
}
