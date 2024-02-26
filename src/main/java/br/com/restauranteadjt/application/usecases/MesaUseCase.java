package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;

import java.util.List;

public class MesaUseCase {
    private final MesaGateway mesaGateway;

    public MesaUseCase(MesaGateway mesaGateway) {
        this.mesaGateway = mesaGateway;
    }

    public List<MesaDomain> create(String idRestaurante) {
        return mesaGateway.create(idRestaurante);
    }

    public MesaDomain findMesaByIdRestauranteAndNumeroMesa(String idRestaurante, Integer numeroMesa) {
        return mesaGateway.findMesaByIdRestauranteAndNumeroMesa(idRestaurante, numeroMesa);
    }

}
