package br.com.restauranteadjt.infrastructure.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
//    private final ReservaInteractor reservaInteractor;
//    private final ReservaDTOMapper reservaDTOMapper;
//    private final RestauranteInteractor restauranteInteractor;
//    private final MesaInteractor mesaInteractor;
//
//    public ReservaController(ReservaInteractor reservaInteractor, ReservaDTOMapper reservaDTOMapper,
//                             RestauranteInteractor restauranteInteractor, MesaInteractor mesaInteractor) {
//        this.reservaInteractor = reservaInteractor;
//        this.reservaDTOMapper = reservaDTOMapper;
//        this.restauranteInteractor = restauranteInteractor;
//        this.mesaInteractor = mesaInteractor;
//    }
//
//    @PostMapping("{idRestaurante}/{numeroMesa}")
//    public ResponseEntity<Object> create(
//            @PathVariable String idRestaurante,
//            @PathVariable Integer numeroMesa,
//            @RequestBody CreateReservaRequest request) {
//
//        RestauranteDomain restauranteDomain = restauranteInteractor.findById(idRestaurante);
//
//        MesaDomain mesaDomain = mesaInteractor.findMesaByIdRestauranteAndNumeroMesa(idRestaurante, numeroMesa);
//
//        ReservaDomain reservaDomain = reservaDTOMapper.toDomain(request);
//
//        ReservaDomain reserva = reservaInteractor.createReserva(reservaDomain, restauranteDomain, mesaDomain);
//
//        return null;
//    }
}
