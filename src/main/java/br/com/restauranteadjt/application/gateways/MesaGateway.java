package br.com.restauranteadjt.application.gateways;

import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;

import java.util.List;

public interface MesaGateway {

    List<MesaDomain> create(String idRestaurante);

    MesaDomain findMesaByIdRestauranteAndNumeroMesa(String idRestaurante, Integer numeroMesa);

    List<MesaDomain> listMesasByStatus(String idRestaurante, StatusMesa statusMesa);

    MesaDomain updateStatusMesa(String idRestaurante, Integer numeroMesa, StatusMesa statusMesa);
}
