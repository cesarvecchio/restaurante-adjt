package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.MesaUseCase;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.controllers.dto.MesaResponse;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.StatusMesaRequest;
import br.com.restauranteadjt.infrastructure.controllers.mapper.MesaDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.MesaPresenter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
        @PathVariable String idRestaurante, @PathVariable LocalDate dataReserva, @PathVariable LocalTime horaReserva,
        @RequestParam(required = false) StatusMesa statusMesa
    ) {
        List<MesaDomain> mesaDomainList = mesaUseCase.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
            idRestaurante, dataReserva, horaReserva, statusMesa
        );

        List<MesaResponse> responseList = mesaDomainList.stream().map(mesaDTOMapper::toResponse).toList();

        return mesaPresenter.toResponseEntity(responseList, HttpStatus.OK);
    }

    @PutMapping("/{idReserva}")
    public ResponseEntity<MesaResponse> update(@PathVariable String idReserva, @RequestBody StatusMesaRequest statusMesa) {
        MesaDomain mesaDomain = mesaUseCase.update(idReserva, statusMesa.getStatusMesa());

        MesaResponse mesaResponse = mesaDTOMapper.toResponse(mesaDomain);

        return mesaPresenter.toResponseEntity(mesaResponse, HttpStatusCode.valueOf(202));
    }
}
