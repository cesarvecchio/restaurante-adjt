package br.com.restauranteadjt.infrastructure.presenter;

import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class MesaPresenter {
    public ResponseEntity<List<MesaResponse>> toResponseEntity(List<MesaResponse> mesaResponseList, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(mesaResponseList);
    }
}
