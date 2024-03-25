package br.com.restauranteadjt.application.usecases;

import br.com.restauranteadjt.application.gateways.MesaGateway;
import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MesaUseCase {
    private final MesaGateway mesaGateway;

    public MesaUseCase(MesaGateway mesaGateway) {
        this.mesaGateway = mesaGateway;
    }

    public List<MesaDomain> listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(
        String idRestaurante, LocalDate dataReserva, LocalTime horaReserva, StatusMesa statusMesa) {

        return mesaGateway.listMesasByIdRestauranteAndDataReservaAndHoraReservaAndStatusMesa(idRestaurante,
            dataReserva, horaReserva, statusMesa);
    }

    public MesaDomain update(String idReserva, StatusMesa statusMesa) {
        return mesaGateway.update(idReserva, statusMesa);
    }
}
