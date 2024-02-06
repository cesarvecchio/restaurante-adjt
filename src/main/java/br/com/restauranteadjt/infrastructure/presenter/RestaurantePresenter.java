package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.CreateRestauranteResponse;
import org.springframework.http.ResponseEntity;

public class RestaurantePresenter {
    public ResponseEntity<CreateRestauranteResponse> toResponseEntity(CreateRestauranteResponse domainObj){
        return ResponseEntity.ok(domainObj);
    }
}
