package br.com.restauranteadjt.infrastructure.gateways.mapper;

import br.com.restauranteadjt.domain.entity.MesaDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.MesaVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MesaVOMapperTest {
    private final MesaVOMapper mesaVOMapper = new MesaVOMapper();

    @Test
    void deveConverterDomainParaVO(){
        var domain = new MesaDomain(
                "1",
                "teste@teste.com",
                StatusMesa.LIVRE
        );

        var vo = mesaVOMapper.toVO(domain);

        assertEquals(domain.id(), vo.getId());
        assertEquals(domain.emailCliente(), vo.getEmailCliente());
        assertEquals(domain.statusMesa(), vo.getStatusMesa());
    }

    @Test
    void deveConverterVOParaDomain(){
        var vo = new MesaVO(
                "1",
                "teste@teste.com",
                StatusMesa.LIVRE
        );

        var domain = mesaVOMapper.toDomain(vo);

        assertEquals(vo.getId(), domain.id());
        assertEquals(vo.getEmailCliente(), domain.emailCliente());
        assertEquals(vo.getStatusMesa(), domain.statusMesa());
    }
}
