package br.com.restauranteadjt.infrastructure.persistence.collection;

import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.RestauranteVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "Reserva")
@Data
@NoArgsConstructor
public class ReservaCollection {
    @Id
    private String id;
    LocalDate dataReserva;
    LocalTime horaReserva;
    RestauranteVO restauranteVO;
    MesaVO mesa;

}
