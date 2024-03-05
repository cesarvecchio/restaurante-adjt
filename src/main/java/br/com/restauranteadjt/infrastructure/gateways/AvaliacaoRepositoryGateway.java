package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.AvaliacaoGateway;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.domain.enums.StatusMesa;
import br.com.restauranteadjt.infrastructure.gateways.mapper.AvaliacaoVOMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.ReservaCollection;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.ReservaRepository;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.infrastructure.persistence.valueObjects.AvaliacaoVO;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import br.com.restauranteadjt.main.exception.StatusReservaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AvaliacaoRepositoryGateway implements AvaliacaoGateway {
    private final RestauranteRepository restauranteRepository;
    private final RestauranteRepositoryGateway restauranteRepositoryGateway;
    private final ReservaRepository reservaRepository;
    private final AvaliacaoVOMapper avaliacaoVOMapper;

    public AvaliacaoRepositoryGateway(RestauranteRepository restauranteRepository,
                                      RestauranteRepositoryGateway restauranteRepositoryGateway,
                                      ReservaRepository reservaRepository, AvaliacaoVOMapper avaliacaoVOMapper) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteRepositoryGateway = restauranteRepositoryGateway;
        this.reservaRepository = reservaRepository;
        this.avaliacaoVOMapper = avaliacaoVOMapper;
    }

    @Override
    public AvaliacaoDomain create(String idReserva, AvaliacaoDomain avaliacaoDomain) {
        ReservaCollection reservaCollection = reservaRepository.findById(idReserva).orElseThrow(() ->
                new NaoEncontradoException(String.format("Reserva com id:'%s' não foi encontrada", idReserva)));

        if(!reservaCollection.getStatusMesa().equals(StatusMesa.FINALIZADA)) {
            throw new StatusReservaException(String.format(
                    "A Reserva com id:'%s' possui o status:'%s', só é possivel avaliar o resturante quando o status estiver como:'%s'",
                    idReserva, reservaCollection.getStatusMesa(), StatusMesa.FINALIZADA));
        }

        RestauranteCollection restauranteCollection = restauranteRepository.findById(
                reservaCollection.getRestaurante().id()).get();

        List<AvaliacaoVO> avaliacaoList = new ArrayList<>();

        if(!Objects.isNull(restauranteCollection.getAvaliacoes()) &&
                !restauranteCollection.getAvaliacoes().isEmpty()) {
            avaliacaoList.addAll(restauranteCollection.getAvaliacoes());
        }

        AvaliacaoVO avaliacaoVO = new AvaliacaoVO(avaliacaoDomain.pontos(), avaliacaoDomain.comentarios());
        avaliacaoVO.setIdReserva(reservaCollection.getId());

        avaliacaoList.add(avaliacaoVO);

        restauranteCollection.setAvaliacoes(avaliacaoList);

        restauranteRepository.save(restauranteCollection);

        return avaliacaoVOMapper.toDomain(avaliacaoVO);
    }

    @Override
    public List<AvaliacaoDomain> listByIdRestaurante(String idRestaurante) {
        RestauranteCollection restauranteCollection = restauranteRepositoryGateway
                .findRestauranteCollection(idRestaurante);

        if(restauranteCollection.getAvaliacoes() == null || restauranteCollection.getAvaliacoes().isEmpty()) {
            throw new StatusReservaException(String.format(
                    "O Restaurante com id:'%s' não possui nenhuma avaliação até o momento",
                    idRestaurante));
        }

        return restauranteCollection.getAvaliacoes().stream().map(avaliacaoVOMapper::toDomain).toList();
    }
}
