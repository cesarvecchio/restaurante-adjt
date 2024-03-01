package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.MesaUseCase;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import br.com.restauranteadjt.infrastructure.controllers.mapper.MesaDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.MesaPresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/mesas")
public class MesaController {
    private final MesaUseCase mesaUseCase;
    private final MesaDTOMapper mesaDTOMapper;
    private final MesaPresenter mesaPresenter;

    public MesaController(MesaUseCase mesaUseCase, MesaDTOMapper mesaDTOMapper, MesaPresenter mesaPresenter) {
        this.mesaUseCase = mesaUseCase;
        this.mesaDTOMapper = mesaDTOMapper;
        this.mesaPresenter = mesaPresenter;
    }

    @GetMapping("/{idRestaurante}/{dataReserva}/{horaReserva}")
    public ResponseEntity<List<MesaResponse>> listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
            @PathVariable String idRestaurante, @PathVariable LocalDate dataReserva, @PathVariable LocalTime horaReserva,
            @RequestParam(required = false) StatusMesa statusMesa
    ){
        List<MesaDomain> mesaDomainList = mesaUseCase.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
                idRestaurante, dataReserva, horaReserva, statusMesa
        );

        List<MesaResponse> responseList = mesaDomainList.stream().map(mesaDTOMapper::toResponse).toList();

        return mesaPresenter.toResponseEntity(responseList, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<MesaResponse> update(@PathVariable String idReserva, @RequestBody StatusMesa statusMesa) {
        MesaDomain mesaDomain = mesaUseCase.update(idReserva, statusMesa);

        MesaResponse mesaResponse = mesaDTOMapper.toResponse(mesaDomain);

        return mesaPresenter.toResponseEntity(mesaResponse, HttpStatusCode.valueOf(202));
    }
}
