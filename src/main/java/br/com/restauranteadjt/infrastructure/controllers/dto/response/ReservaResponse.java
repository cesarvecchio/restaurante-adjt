package br.com.restauranteadjt.infrastructure.controllers.dto.response;

import br.com.restauranteadjt.domain.enums.StatusMesa;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaResponse(
        String id,
        LocalDate dataReserva,
        LocalTime horaReserva/*,
        Restaurante restaurante,
        Mesa mesa*/
) {
//    public record Restaurante(
//            String id,
//            String nome
//    ){}
//
//    public record Mesa(
//            Integer identificador,
//            StatusMesa statusMesa,
//            Integer qtdePessoas
//    ){}
}
