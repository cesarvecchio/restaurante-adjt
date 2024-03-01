package br.com.restauranteadjt.infrastructure.persistence.collection;

import br.com.restauranteadjt.domain.enums.StatusMesa;
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
    private LocalDate dataReserva;
    private LocalTime horaReserva;
    private String nome;
    private String email;
    private String telefone;
    private RestauranteVO restaurante;
    private StatusMesa statusMesa;

    public ReservaCollection(LocalDate dataReserva, LocalTime horaReserva, String nome, String email, String telefone) {
        this.dataReserva = dataReserva;
        this.horaReserva = horaReserva;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
}
