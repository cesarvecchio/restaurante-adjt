package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class MesaPresenter {
    public ResponseEntity<List<MesaResponse>> toResponseEntity(List<MesaResponse> mesaResponseList, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(mesaResponseList);
    }

    public ResponseEntity<MesaResponse> toResponseEntity(MesaResponse mesaResponse, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(mesaResponse);
    }
}
