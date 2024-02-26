package br.com.restauranteadjt.domain.entity;

import br.com.restauranteadjt.domain.valueObject.Endereco;

import java.time.LocalTime;
import java.util.List;

public record RestauranteDomain(
        String nome,
        Endereco localizacao,
        String tipoCozinha,
        List<LocalTime> horariosFuncionamento,
        Integer capacidade
        ) {
}
