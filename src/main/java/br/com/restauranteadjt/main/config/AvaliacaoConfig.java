package br.com.restauranteadjt.main.config;

import br.com.restauranteadjt.application.gateways.AvaliacaoGateway;
import br.com.restauranteadjt.application.usecases.AvaliacaoUseCase;
import br.com.restauranteadjt.infrastructure.controllers.mapper.AvaliacaoDTOMapper;
import br.com.restauranteadjt.infrastructure.gateways.AvaliacaoRepositoryGateway;
import br.com.restauranteadjt.infrastructure.gateways.mapper.AvaliacaoVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.presenter.AvaliacaoPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvaliacaoConfig {

    @Bean
    AvaliacaoUseCase avaliacaoUseCase(AvaliacaoGateway avaliacaoGateway) {
        return new AvaliacaoUseCase(avaliacaoGateway);
    }

    @Bean
    AvaliacaoGateway avaliacaoGateway(RestauranteRepository restauranteRepository,
                                      ReservaRepository reservaRepository,
                                      AvaliacaoVOMapper avaliacaoVOMapper) {

        return new AvaliacaoRepositoryGateway(restauranteRepository, reservaRepository, avaliacaoVOMapper);
    }

    @Bean
    AvaliacaoVOMapper avaliacaoVOMapper() {
        return new AvaliacaoVOMapper();
    }

    @Bean
    AvaliacaoDTOMapper avaliacaoDTOMapper() {
        return new AvaliacaoDTOMapper();
    }

    @Bean
    AvaliacaoPresenter avaliacaoPresenter() {
        return new AvaliacaoPresenter();
    }
}
