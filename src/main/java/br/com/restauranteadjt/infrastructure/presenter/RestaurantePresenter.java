package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class RestaurantePresenter {
    public ResponseEntity<RestauranteResponse> toResponseEntity(RestauranteResponse restauranteResponse, HttpStatusCode statusCode){
        return ResponseEntity.status(statusCode).body(restauranteResponse);
    }
}
