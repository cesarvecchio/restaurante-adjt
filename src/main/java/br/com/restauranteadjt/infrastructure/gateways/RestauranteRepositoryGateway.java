package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.domain.enums.TipoCozinhaEnum;
import br.com.restauranteadjt.domain.valueObject.Endereco;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestauranteRepositoryGateway implements RestauranteGateway {
    private final RestauranteRepository restauranteRepository;
    private final RestauranteCollectionMapper restauranteCollectionMapper;

    public RestauranteRepositoryGateway(RestauranteRepository restauranteRepository,
                                        RestauranteCollectionMapper restauranteCollectionMapper) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteCollectionMapper = restauranteCollectionMapper;
    }

    @Override
    public RestauranteDomain create(RestauranteDomain restauranteDomain) {
        RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);

        RestauranteCollection savedRestauranteObj = restauranteRepository.save(restauranteCollection);

        return restauranteCollectionMapper.toDomainObj(savedRestauranteObj);
    }

    @Override
    public RestauranteDomain findByNomeOrTipoCozinhaOrLocalizacao(String nome, TipoCozinhaEnum tipoCozinha, Endereco localizacao) {
        Optional<RestauranteCollection> restauranteCollection = restauranteRepository
                .findRestauranteByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, localizacao);
        return restauranteCollectionMapper.toDomainObj(restauranteCollection.orElseThrow(() -> new RuntimeException("Restaurante não encontrado")));
    }

    @Override
    public RestauranteDomain findById(String id) {
        Optional<RestauranteCollection> restauranteCollection = restauranteRepository.findById(id);

        return restauranteCollectionMapper.toDomainObj(restauranteCollection.orElseThrow(() -> new RuntimeException("Restaurante não encontrado")));
    }
}
