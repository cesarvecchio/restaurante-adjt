package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ReservaPresenter {
    public ResponseEntity<ReservaResponse> toResponseEntity(ReservaResponse response, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(response);
    }
}
