package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.main.exception.CadastradoException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

public class RestauranteRepositoryGateway implements RestauranteGateway {
    private final RestauranteRepository restauranteRepository;
    private final RestauranteCollectionMapper restauranteCollectionMapper;
    private final MongoTemplate mongoTemplate;

    public RestauranteRepositoryGateway(RestauranteRepository restauranteRepository,
                                        RestauranteCollectionMapper restauranteCollectionMapper,
                                        MongoTemplate mongoTemplate) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteCollectionMapper = restauranteCollectionMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public RestauranteDomain create(RestauranteDomain restauranteDomain) {
        RestauranteCollection restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);

        if(!findByNomeOrTipoCozinhaOrLocalizacao(restauranteCollection.getNome(),
                restauranteCollection.getTipoCozinha(), restauranteCollection.getLocalizacao()).isEmpty()){
            throw new CadastradoException(String.format(
                    "Restaurante com nome:'%s', tipoCozinha:'%s' e localizacao:'%s' já está cadastrado no sistema",
                    restauranteCollection.getNome(),restauranteCollection.getTipoCozinha(),
                    restauranteCollection.getLocalizacao()));
        }

        RestauranteCollection savedRestauranteObj = restauranteRepository.save(restauranteCollection);

        return restauranteCollectionMapper.toDomainObj(savedRestauranteObj);
    }

    @Override
    public RestauranteDomain findById(String id) {
        return restauranteCollectionMapper.toDomainObj(restauranteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                String.format("Restaurante com id:%s não foi encontrado!", id))));
    }

    @Override
    public List<RestauranteDomain> findAll() {
        return restauranteRepository.findAll().stream().map(restauranteCollectionMapper::toDomainObj).toList();
    }

    @Override
    public List<RestauranteDomain> findByNomeOrTipoCozinhaOrLocalizacao(String nome, String tipoCozinha, String localizacao) {
        Query query = new Query();

        if(!ObjectUtils.isEmpty(nome)){
            Criteria criteria = Criteria.where("nome").in(nome);
            query.addCriteria(criteria);
        }
        if(!ObjectUtils.isEmpty(tipoCozinha)){
            Criteria criteria = Criteria.where("tipoCozinha").in(tipoCozinha);
            query.addCriteria(criteria);
        }
        if(!ObjectUtils.isEmpty(localizacao)){
            Criteria criteria = Criteria.where("localizacao").in(localizacao);
            query.addCriteria(criteria);
        }

        return mongoTemplate.find(query, RestauranteCollection.class)
                .stream()
                .map(restauranteCollectionMapper::toDomainObj)
                .toList();
    }

    protected RestauranteCollection findRestauranteCollection(String idRestaurante){
        return restauranteRepository.findById(idRestaurante)
                .orElseThrow(() ->
                        new RuntimeException(
                                String.format("Restaurante com id:%s não foi encontrado!", idRestaurante)));
    }
}
