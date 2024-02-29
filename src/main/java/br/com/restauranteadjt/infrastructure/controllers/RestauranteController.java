package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.RestauranteUseCase;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import br.com.restauranteadjt.infrastructure.controllers.mapper.RestauranteDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.RestaurantePresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteUseCase restauranteUseCase;
    private final RestauranteDTOMapper restauranteDTOMapper;
    private final RestaurantePresenter restaurantePresenter;

    public RestauranteController(RestauranteUseCase restauranteUseCase,
                                 RestauranteDTOMapper restauranteDTOMapper,
                                 RestaurantePresenter restaurantePresenter) {
        this.restauranteUseCase = restauranteUseCase;
        this.restauranteDTOMapper = restauranteDTOMapper;
        this.restaurantePresenter = restaurantePresenter;
    }

    @PostMapping
    public ResponseEntity<RestauranteResponse> create(@RequestBody CreateRestauranteRequest request) {
        RestauranteDomain restauranteDomain = restauranteDTOMapper.toRestauranteDomain(request);

        RestauranteDomain restaurante = restauranteUseCase.create(restauranteDomain);

        RestauranteResponse response = restauranteDTOMapper.toResponse(restaurante);

        return restaurantePresenter.toResponseEntity(response, HttpStatusCode.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> findByNomeOrTipoCozinhaOrLocalizacao(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipoCozinha,
            @RequestParam(required = false) String endereco
    ){
        List<RestauranteDomain> restauranteList = restauranteUseCase
                .findByNomeOrTipoCozinhaOrLocalizacao(nome, tipoCozinha, endereco);

       List<RestauranteResponse> restauranteResponseList = restauranteList.stream().map(restauranteDTOMapper::toResponse).toList();

        return restaurantePresenter.toResponseEntity(restauranteResponseList, HttpStatusCode.valueOf(200));
    }
}
