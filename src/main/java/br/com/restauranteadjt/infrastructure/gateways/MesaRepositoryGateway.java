package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.application.usecases.RestauranteUseCase;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.MesaVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusMesaException;

import java.util.ArrayList;
import java.util.List;

public class MesaRepositoryGateway implements MesaGateway {
    private final MesaVOMapper mesaVOMapper;
    private final RestauranteRepository restauranteRepository;

    public MesaRepositoryGateway(MesaVOMapper mesaVOMapper, RestauranteRepository restauranteRepository) {
        this.mesaVOMapper = mesaVOMapper;
        this.restauranteRepository = restauranteRepository;
    }

    private RestauranteCollection validateMesas(String idRestaurante){
        RestauranteCollection restauranteCollection = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new NaoEncontradoException(
                        String.format("Restaurante com id:%s não foi encontrado!", idRestaurante)));

        if(restauranteCollection.getMesas() == null || restauranteCollection.getMesas().isEmpty()){
            throw new NaoEncontradoException(
                    String.format("Restaurante com o id:%s não possui nenhuma mesa registrada!", idRestaurante));
        }

        return restauranteCollection;
    }

    @Override
    public List<MesaDomain> create(String idRestaurante){
        RestauranteCollection restauranteCollection = restauranteRepository.findById(idRestaurante)
                .orElseThrow(() -> new NaoEncontradoException(
                        String.format("Restaurante com id:%s não foi encontrado!", idRestaurante)));

        if(restauranteCollection.getMesas() != null
            && !restauranteCollection.getMesas().isEmpty()
            && restauranteCollection.getCapacidade() == restauranteCollection.getMesas().size()
        ) {
            throw new RuntimeException(
                    String.format("Restaurante com id:%s já possui a quantidade maxima de mesas registradas!",
                            idRestaurante));
        }

        List<MesaVO> listaMesas = new ArrayList<>();
        if(restauranteCollection.getMesas() != null) {
            listaMesas.addAll(restauranteCollection.getMesas());
        }

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

    @Override
    public List<MesaDomain> listMesasByStatus(String idRestaurante, StatusMesa statusMesa) {
        RestauranteCollection restauranteCollection = validateMesas(idRestaurante);

        List<MesaVO> mesaVOList = restauranteCollection.getMesas();

        if(statusMesa != null) {
            mesaVOList = restauranteCollection.getMesas().stream().filter(mesa ->
                    mesa.getStatusMesa().equals(statusMesa)).toList();

            if(mesaVOList.isEmpty()){
                throw new NaoEncontradoException(
                        String.format("Restaurante com o id:%s não possui nenhuma mesa com o status:%s no momento!",
                                idRestaurante, statusMesa));
            }
        }

        return mesaVOList.stream().map(mesaVOMapper::toDomain).toList();
    }

    @Override
    public MesaDomain updateStatusMesa(String idRestaurante, Integer numeroMesa, StatusMesa statusMesa) {
        RestauranteCollection restauranteCollection = validateMesas(idRestaurante);

        MesaVO mesaVO = restauranteCollection.getMesas().stream().filter(mesa -> mesa.getNumero().equals(numeroMesa))
                .findFirst().orElseThrow(() -> new NaoEncontradoException(
                        String.format("Mesa de numero: '%d' não encontrada para o Restaurante com Id: '%s''",
                                numeroMesa, idRestaurante)));

        if(mesaVO.getStatusMesa().equals(statusMesa)) {
            throw new StatusMesaException(
                    String.format("Mesa numero: '%d' do Restaurante com Id: '%s' já possui o Status: '%s'",
                            numeroMesa, idRestaurante, statusMesa));
        }

        mesaVO.setStatusMesa(statusMesa);

        restauranteCollection.getMesas().forEach(mesa -> {
            if(mesa.getNumero().equals(numeroMesa)){
                mesa = mesaVO;
            }
        });

        restauranteRepository.save(restauranteCollection);

        return mesaVOMapper.toDomain(mesaVO);
    }


}
