package br.com.restauranteadjt.main.config;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.application.usecases.RestauranteUseCase;
import br.com.restauranteadjt.infrastructure.controllers.mapper.RestauranteDTOMapper;
import br.com.restauranteadjt.infrastructure.gateways.RestauranteRepositoryGateway;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.presenter.RestaurantePresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class RestauranteConfig {
    @Bean
    RestauranteUseCase restauranteUseCase(RestauranteGateway restauranteGateway) {
        return new RestauranteUseCase(restauranteGateway);
    }

    @Bean
    RestauranteGateway restauranteGateway(RestauranteRepository restauranteRepository,
                                          RestauranteCollectionMapper restauranteCollectionMapper,
                                          MongoTemplate mongoTemplate) {
        return new RestauranteRepositoryGateway(restauranteRepository, restauranteCollectionMapper, mongoTemplate);
    }

    @Bean
    RestauranteCollectionMapper restauranteCollectionMapper(){
        return new RestauranteCollectionMapper();
    }

    @Bean
    RestauranteDTOMapper restauranteDTOMapper(){
        return new RestauranteDTOMapper();
    }

    @Bean
    RestaurantePresenter restaurantePresenter(){
        return new RestaurantePresenter();
    }

    @Bean
    RestauranteRepositoryGateway restauranteRepositoryGateway(RestauranteRepository restauranteRepository,
                                                              RestauranteCollectionMapper restauranteCollectionMapper,
                                                              MongoTemplate mongoTemplate){
        return new RestauranteRepositoryGateway(restauranteRepository, restauranteCollectionMapper, mongoTemplate);
    }
}
