package br.com.restauranteadjt.infrastructure.persistence.repository;

import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RestauranteRepository extends MongoRepository<RestauranteCollection, String> {
}
