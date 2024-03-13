package br.com.restauranteadjt.infrastructure.controllers.dto.request;

import br.com.restauranteadjt.domain.enums.StatusMesa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusMesaRequest {
    private StatusMesa statusMesa;
}
