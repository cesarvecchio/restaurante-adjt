package br.com.restauranteadjt.infrastructure.persistence.valueObjects;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaVO {
    private Integer numero;
    private StatusMesa statusMesa;
}
