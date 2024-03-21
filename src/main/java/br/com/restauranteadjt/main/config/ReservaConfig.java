package br.com.restauranteadjt.main.config;

import br.com.restauranteadjt.application.gateways.ReservaGateway;
import br.com.restauranteadjt.application.usecases.ReservaUseCase;
import br.com.restauranteadjt.infrastructure.controllers.mapper.ReservaDTOMapper;
import br.com.restauranteadjt.infrastructure.gateways.ReservaRepositoryGateway;
import br.com.restauranteadjt.infrastructure.gateways.mapper.ReservaColletionMapper;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.presenter.ReservaPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservaConfig {
    @Bean
    ReservaUseCase reservaUseCase(ReservaGateway reservaGateway) {
        return new ReservaUseCase(reservaGateway);
    }

    @Bean
    ReservaGateway reservaGateway(RestauranteRepository restauranteRepository,
                                  ReservaRepository reservaRepository,
                                  ReservaColletionMapper reservaColletionMapper) {
        return new ReservaRepositoryGateway(restauranteRepository,
            reservaRepository, reservaColletionMapper);
    }

    @Bean
    ReservaColletionMapper reservaColletionMapper() {
        return new ReservaColletionMapper();
    }

    @Bean
    ReservaPresenter reservaPresenter() {
        return new ReservaPresenter();
    }

    @Bean
    ReservaDTOMapper reservaDTOMapper() {
        return new ReservaDTOMapper();
    }
}
