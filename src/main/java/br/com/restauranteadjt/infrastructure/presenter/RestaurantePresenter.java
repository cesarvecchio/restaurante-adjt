package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.RestauranteResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class RestaurantePresenter {
    public ResponseEntity<RestauranteResponse> toResponseEntity(RestauranteResponse restauranteResponse, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(restauranteResponse);
    }

    public ResponseEntity<List<RestauranteResponse>> toResponseEntity(List<RestauranteResponse> restauranteResponseList, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(restauranteResponseList);
    }
}
