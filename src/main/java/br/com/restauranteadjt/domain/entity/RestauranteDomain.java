package br.com.restauranteadjt.domain.entity;

import java.time.LocalTime;
import java.util.List;

public record RestauranteDomain(
        String nome,
        String localizacao,
        String tipoCozinha,
        List<LocalTime> horariosFuncionamento,
        Integer capacidade
        ) {
}
