package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.MesaVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;

import java.util.ArrayList;
import java.util.List;

public class MesaRepositoryGateway implements MesaGateway {
    private final MesaVOMapper mesaVOMapper;
    private final RestauranteRepository restauranteRepository;

    public MesaRepositoryGateway(MesaVOMapper mesaVOMapper, RestauranteRepository restauranteRepository) {
        this.mesaVOMapper = mesaVOMapper;
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public List<MesaDomain> create(String idRestaurante){
        RestauranteCollection restauranteCollection = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Restaurante com id:%s não foi encontrado!", idRestaurante)));

        if(restauranteCollection.getCapacidade() == restauranteCollection.getMesas().size()
        && !restauranteCollection.getMesas().isEmpty()) {
            throw new RuntimeException(
                    String.format("Restaurante com id:%s já possui a quantidade maxima de mesas registradas!",
                            idRestaurante));
        }

        List<MesaVO> listaMesas = new ArrayList<>(restauranteCollection.getMesas());
        int contador = listaMesas.size();

        while(restauranteCollection.getCapacidade() > contador) {
            contador++;

            listaMesas.add(new MesaVO(contador, StatusMesa.LIVRE));
        }

        restauranteCollection.setMesas(listaMesas);

        restauranteRepository.save(restauranteCollection);

        return listaMesas.stream().map(mesaVOMapper::toDomain).toList();
    }

    @Override
    public MesaDomain findMesaByIdRestauranteAndNumeroMesa(String idRestaurante, Integer numeroMesa) {

        return null;
    }

}
