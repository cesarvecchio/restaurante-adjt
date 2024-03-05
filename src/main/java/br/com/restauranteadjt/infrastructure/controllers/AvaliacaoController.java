package br.com.restauranteadjt.infrastructure.controllers;

import br.com.restauranteadjt.application.usecases.AvaliacaoUseCase;
import br.com.restauranteadjt.domain.entity.AvaliacaoDomain;
import br.com.restauranteadjt.infrastructure.controllers.dto.request.AvaliacaoRequest;
import br.com.restauranteadjt.infrastructure.controllers.dto.response.AvaliacaoResponse;
import br.com.restauranteadjt.infrastructure.controllers.mapper.AvaliacaoDTOMapper;
import br.com.restauranteadjt.infrastructure.presenter.AvaliacaoPresenter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    private final AvaliacaoDTOMapper avaliacaoDTOMapper;
    private final AvaliacaoUseCase avaliacaoUseCase;
    private final AvaliacaoPresenter avaliacaoPresenter;

    public AvaliacaoController(AvaliacaoDTOMapper avaliacaoDTOMapper,
                               AvaliacaoUseCase avaliacaoUseCase,
                               AvaliacaoPresenter avaliacaoPresenter) {
        this.avaliacaoDTOMapper = avaliacaoDTOMapper;
        this.avaliacaoUseCase = avaliacaoUseCase;
        this.avaliacaoPresenter = avaliacaoPresenter;
    }

    @PostMapping("/{idReserva}")
    public ResponseEntity<AvaliacaoResponse> create(@PathVariable String idReserva, @RequestBody AvaliacaoRequest avaliacaoRequest) {
        AvaliacaoDomain avaliacaoDomain = avaliacaoDTOMapper.toDomain(avaliacaoRequest);

        AvaliacaoDomain avaliacao = avaliacaoUseCase.create(idReserva, avaliacaoDomain);

        AvaliacaoResponse response = avaliacaoDTOMapper.toResponse(avaliacao);

        return avaliacaoPresenter.toResponseEntity(response, HttpStatusCode.valueOf(201));
    }

    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<AvaliacaoResponse>> listByIdRestaurante(@PathVariable String idRestaurante) {
        List<AvaliacaoDomain> avaliacaoList = avaliacaoUseCase.listByIdRestaurante(idRestaurante);

        List<AvaliacaoResponse> response = avaliacaoList.stream().map(avaliacaoDTOMapper::toResponse).toList();

        return avaliacaoPresenter.toResponseEntity(response, HttpStatusCode.valueOf(201));
    }
}
