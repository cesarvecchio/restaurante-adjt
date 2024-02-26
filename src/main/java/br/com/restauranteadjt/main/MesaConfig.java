package br.com.restauranteadjt.main;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.application.usecases.MesaUseCase;
import br.com.restauranteadjt.infrastructure.controllers.mapper.MesaDTOMapper;
import br.com.restauranteadjt.infrastructure.gateways.MesaRepositoryGateway;
import br.com.restauranteadjt.infrastructure.gateways.mapper.MesaVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.presenter.MesaPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MesaConfig {
    @Bean
    MesaUseCase mesaUseCase(MesaGateway mesaGateway){
        return new MesaUseCase(mesaGateway);
    }

    @Bean
    MesaGateway mesaGateway(MesaVOMapper mesaVOMapper, RestauranteRepository restauranteRepository){
        return new MesaRepositoryGateway(mesaVOMapper, restauranteRepository);
    }

    @Bean
    MesaVOMapper mesaVOMapper() {
        return new MesaVOMapper();
    }

    @Bean
    MesaDTOMapper mesaDTOMapper() {
        return new MesaDTOMapper();
    }

    @Bean
    MesaPresenter mesaPresenter() {
        return new MesaPresenter();
    }
}
