package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.domain.enums.TipoCozinhaEnum;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
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

        RestauranteCollection savedRestauranteObj = restauranteRepository.save(restauranteCollection);

        return restauranteCollectionMapper.toDomainObj(savedRestauranteObj);
    }

    @Override
    public RestauranteDomain findById(String id) {
        Optional<RestauranteCollection> restauranteCollection = restauranteRepository.findById(id);

        return restauranteCollectionMapper.toDomainObj(restauranteCollection.orElseThrow(() -> new RuntimeException("Restaurante não encontrado")));
    }

    @Override
    public List<RestauranteDomain> findAll() {
        return restauranteRepository.findAll().stream().map(restauranteCollectionMapper::toDomainObj).toList();
    }

    @Override
    public List<RestauranteDomain> findByNomeOrTipoCozinhaOrLocalizacao(String nome, TipoCozinhaEnum tipoCozinha, String localizacao) {
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
}
