package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;

public class MesaVOMapper {
    public MesaVO toVO(MesaDomain mesaDomain) {
        return new MesaVO(
                mesaDomain.id(),
                mesaDomain.emailCliente(),
                mesaDomain.statusMesa()
        );
    }

    public MesaDomain toDomain(MesaVO mesaVO) {
        return new MesaDomain(
                mesaVO.getId(),
                mesaVO.getEmailCliente(),
                mesaVO.getStatusMesa()
        );
    }
}
