package br.com.restauranteadjt.domain.valueObject;

import lombok.*;


public record Endereco(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String numero,
        Double latitude,
        Double longitude) {
}

