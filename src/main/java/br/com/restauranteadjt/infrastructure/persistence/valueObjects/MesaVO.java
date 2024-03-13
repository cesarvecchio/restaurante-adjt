package br.com.restauranteadjt.infrastructure.persistence.valueObjects;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MesaVO {
    private String id;
    private String emailCliente;
    private StatusMesa statusMesa;
}
