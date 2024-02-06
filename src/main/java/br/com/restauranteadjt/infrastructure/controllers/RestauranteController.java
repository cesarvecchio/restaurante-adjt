package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.CreateRestauranteInteractor;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.CreateRestauranteResponse;
import br.com.restauranteadjt.infrastructure.controllers.mapper.RestauranteDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.RestaurantePresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final CreateRestauranteInteractor createRestauranteInteractor;
    private final RestauranteDTOMapper restauranteDTOMapper;
    private final RestaurantePresenter restaurantePresenter;

    public RestauranteController(CreateRestauranteInteractor createRestauranteInteractor,
                                 RestauranteDTOMapper restauranteDTOMapper,
                                 RestaurantePresenter restaurantePresenter) {
        this.createRestauranteInteractor = createRestauranteInteractor;
        this.restauranteDTOMapper = restauranteDTOMapper;
        this.restaurantePresenter = restaurantePresenter;
    }

    @PostMapping
    public ResponseEntity<CreateRestauranteResponse> create(@RequestBody CreateRestauranteRequest request) {
        RestauranteDomain restauranteDomain = restauranteDTOMapper.toRestauranteDomain(request);

        RestauranteDomain restaurante = createRestauranteInteractor.createRestaurante(restauranteDomain);

        CreateRestauranteResponse response = restauranteDTOMapper.toResponse(restaurante);

        return restaurantePresenter.toResponseEntity(response);
    }
}
