package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.MesaDomain;

import java.util.List;

public interface MesaGateway {

    List<MesaDomain> create(String idRestaurante);

    MesaDomain findMesaByIdRestauranteAndNumeroMesa(String idRestaurante, Integer numeroMesa);
}
