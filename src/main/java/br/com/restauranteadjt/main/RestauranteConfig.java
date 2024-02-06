package br.com.restauranteadjt.main;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.application.usecases.CreateRestauranteInteractor;
import br.com.restauranteadjt.infrastructure.controllers.mapper.RestauranteDTOMapper;
import br.com.restauranteadjt.infrastructure.gateways.RestauranteRepositoryGateway;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.presenter.RestaurantePresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestauranteConfig {
    @Bean
    CreateRestauranteInteractor createUseCase(RestauranteGateway restauranteGateway) {
        return new CreateRestauranteInteractor(restauranteGateway);
    }

    @Bean
    RestauranteGateway restauranteGateway(RestauranteRepository restauranteRepository,
                                          RestauranteCollectionMapper restauranteCollectionMapper) {
        return new RestauranteRepositoryGateway(restauranteRepository, restauranteCollectionMapper);
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
}
