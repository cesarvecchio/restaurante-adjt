package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.response.AvaliacaoResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AvaliacaoPresenter {
    public ResponseEntity<List<AvaliacaoResponse>> toResponseEntity(List<AvaliacaoResponse> avaliacaoResponse, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(avaliacaoResponse);
    }

    public ResponseEntity<AvaliacaoResponse> toResponseEntity(AvaliacaoResponse avaliacaoResponse, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(avaliacaoResponse);
    }
}
