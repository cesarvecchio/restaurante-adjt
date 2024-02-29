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

    @PostMapping("/{idRestaurante}")
    public ResponseEntity<List<MesaResponse>> create(@PathVariable String idRestaurante) {
        List<MesaDomain> mesaDomainList = mesaUseCase.create(idRestaurante);

        List<MesaResponse> responseList = mesaDomainList.stream().map(mesaDTOMapper::toResponse).toList();

        return mesaPresenter.toResponseEntity(responseList, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<MesaResponse>> listMesasByStatus(
            @PathVariable String idRestaurante,
            @RequestParam(required = false) StatusMesa statusMesa
    ){
        List<MesaDomain> mesaDomainList = mesaUseCase.listMesasByStatus(idRestaurante, statusMesa);

        List<MesaResponse> responseList = mesaDomainList.stream().map(mesaDTOMapper::toResponse).toList();

        return mesaPresenter.toResponseEntity(responseList, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/{idRestaurante}/{numeroMesa}")
    public ResponseEntity<MesaResponse> updateStatusMesa(
            @PathVariable String idRestaurante,
            @PathVariable Integer numeroMesa,
            /*@RequestParam StatusMesa statusMesa,*/
            @RequestBody StatusMesa statusMesa
    ){
        MesaDomain mesaDomain = mesaUseCase.updateStatusMesa(idRestaurante, numeroMesa, statusMesa);

        MesaResponse mesaResponse = mesaDTOMapper.toResponse(mesaDomain);

        return mesaPresenter.toResponseEntity(mesaResponse, HttpStatusCode.valueOf(200));
    }
}
