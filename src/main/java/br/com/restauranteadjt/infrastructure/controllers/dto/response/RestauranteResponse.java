package br.com.restauranteadjt.infrastructure.controllers.dto.response;

import java.time.LocalTime;
import java.util.List;

public record RestauranteResponse(
    String id,
    String nome,
    String localizacao,
    String tipoCozinha,
    List<LocalTime> horariosFuncionamento,
    Integer capacidade
) {
}
