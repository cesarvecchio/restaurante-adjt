package br.com.restauranteadjt.infrastructure.controllers.mapper;

import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateRestauranteRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;

public class RestauranteDTOMapper {
    public RestauranteResponse toResponse(RestauranteDomain domainObj) {
        return new RestauranteResponse(
                domainObj.getId(),
                domainObj.getNome(),
                domainObj.getLocalizacao(),
                domainObj.getTipoCozinha(),
                domainObj.getHorariosFuncionamento(),
                domainObj.getCapacidade()
        );
    }

    public RestauranteDomain toRestauranteDomain(CreateRestauranteRequest request) {
        return new RestauranteDomain(
                request.id(),
                request.nome(),
                request.localizacao(),
                request.tipoCozinha(),
                request.horariosFuncionamento(),
                request.capacidade()
        );
    }
}
