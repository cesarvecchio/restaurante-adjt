package br.com.restauranteadjt.infrastructure.persistence.repository;

import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestauranteRepository extends MongoRepository<RestauranteCollection, String> {
}
