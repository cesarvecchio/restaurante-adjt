package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.ReservaUseCase;
import br.com.restauranteadjt.domain.entity.ReservaDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.CreateReservaRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.ReservaResponse;
import br.com.restauranteadjt.infrastructure.controllers.mapper.ReservaDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.ReservaPresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaDTOMapper reservaDTOMapper;
    private final ReservaUseCase reservaUseCase;
    private final ReservaPresenter reservaPresenter;

    public ReservaController(ReservaDTOMapper reservaDTOMapper, ReservaUseCase reservaUseCase,
                             ReservaPresenter reservaPresenter) {
        this.reservaDTOMapper = reservaDTOMapper;
        this.reservaUseCase = reservaUseCase;
        this.reservaPresenter = reservaPresenter;
    }

    @PostMapping("{idRestaurante}")
    public ResponseEntity<ReservaResponse> create(
            @PathVariable String idRestaurante,
            @RequestBody CreateReservaRequest request) {
        ReservaDomain reservaDomain = reservaDTOMapper.toDomain(request);

        ReservaDomain reserva = reservaUseCase.create(idRestaurante, reservaDomain);

        ReservaResponse response = reservaDTOMapper.toResponse(reserva);

        return reservaPresenter.toResponseEntity(response, HttpStatusCode.valueOf(201));
    }
}
