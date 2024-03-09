package br.com.restauranteadjt.infrastructure.gateways;

import br.com.restauranteadjt.application.gateways.RestauranteGateway;
import br.com.restauranteadjt.domain.entity.RestauranteDomain;
import br.com.restauranteadjt.infrastructure.gateways.mapper.RestauranteCollectionMapper;
import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import br.com.restauranteadjt.main.exception.CadastradoException;
import br.com.restauranteadjt.main.exception.NaoEncontradoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class RestauranteRepositoryGatewayTest {
    @Mock
    private RestauranteRepository restauranteRepository;
    private final RestauranteCollectionMapper restauranteCollectionMapper = new RestauranteCollectionMapper();
    @Mock
    private MongoTemplate mongoTemplate;

    private RestauranteGateway restauranteGateway;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        restauranteGateway = new RestauranteRepositoryGateway(restauranteRepository, restauranteCollectionMapper,
                mongoTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveGerarExcecao_QuandoCriarRestauranteJaExistente() {
        var restauranteDomain = buildRestauranteDomain();
        var restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);
        var query = buildQuery(restauranteCollection.getNome(), restauranteCollection.getTipoCozinha(),
                restauranteCollection.getLocalizacao());

        when(mongoTemplate.find(query, RestauranteCollection.class)).thenReturn(List.of(restauranteCollection));

        assertThatThrownBy(() -> restauranteGateway.create(restauranteDomain))
                .isInstanceOf(CadastradoException.class)
                .hasMessage(String.format(
                        "Restaurante com nome:'%s', tipoCozinha:'%s' e localizacao:'%s' já está cadastrado no sistema",
                        restauranteCollection.getNome(),restauranteCollection.getTipoCozinha(),
                        restauranteCollection.getLocalizacao()));

        verify(mongoTemplate, times(1)).find(query, RestauranteCollection.class);
    }

    @Test
    void deveCriarRestaurante() {
        var restauranteDomain = buildRestauranteDomain();
        var restauranteCollection = restauranteCollectionMapper.toCollection(restauranteDomain);
        var query = buildQuery(restauranteCollection.getNome(), restauranteCollection.getTipoCozinha(),
                restauranteCollection.getLocalizacao());

        when(mongoTemplate.find(query, RestauranteCollection.class)).thenReturn(List.of());

        when(restauranteRepository.save(restauranteCollection)).thenReturn(restauranteCollection);

        var restauranteDomainRecebido = restauranteGateway.create(restauranteDomain);

        assertEquals(restauranteDomain, restauranteDomainRecebido);

        verify(mongoTemplate, times(1)).find(query, RestauranteCollection.class);
        verify(restauranteRepository, times(1)).save(restauranteCollection);
    }

    @Test
    void deveGerarExcecao_QuandoNaoEncontrarRestaurantePorId(){
        var id = "1";

        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restauranteGateway.findById(id))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format("Restaurante com id:'%s' não foi encontrado!", id));

        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void deveEncontrarRestaurantePorId(){
        var id = "1";
        var restauranteDomain = buildRestauranteDomain();
        var restauranteColletion = restauranteCollectionMapper.toCollection(restauranteDomain);
        restauranteColletion.setId(id);

        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restauranteColletion));

        var restauranteObtido = restauranteGateway.findById(id);

        assertEquals(restauranteDomain.getNome(), restauranteObtido.getNome());
        assertEquals(restauranteDomain.getLocalizacao(), restauranteObtido.getLocalizacao());
        assertEquals(restauranteDomain.getTipoCozinha(), restauranteObtido.getTipoCozinha());
        assertEquals(restauranteDomain.getHorariosFuncionamento(), restauranteObtido.getHorariosFuncionamento());
        assertEquals(restauranteDomain.getCapacidade(), restauranteObtido.getCapacidade());

        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void deveListarTodosOs(){
        var id = "1";
        var restauranteDomain = buildRestauranteDomain();
        var restauranteColletion = restauranteCollectionMapper.toCollection(restauranteDomain);
        restauranteColletion.setId(id);

        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restauranteColletion));

        var restauranteObtido = restauranteGateway.findById(id);

        assertEquals(restauranteDomain.getNome(), restauranteObtido.getNome());
        assertEquals(restauranteDomain.getLocalizacao(), restauranteObtido.getLocalizacao());
        assertEquals(restauranteDomain.getTipoCozinha(), restauranteObtido.getTipoCozinha());
        assertEquals(restauranteDomain.getHorariosFuncionamento(), restauranteObtido.getHorariosFuncionamento());
        assertEquals(restauranteDomain.getCapacidade(), restauranteObtido.getCapacidade());

        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void deveGerarExcecao_QuandoValidarSeRestauranteExistePorId(){
        var id = "1";

        when(restauranteRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> restauranteGateway.existsById(id))
                .isInstanceOf(NaoEncontradoException.class)
                .hasMessage(String.format("Restaurante com id:'%s' não foi encontrado!", id));

        verify(restauranteRepository, times(1)).existsById(id);
    }

    @Test
    void deveValidarSeRestauranteExistePorId(){
        var id = "1";

        when(restauranteRepository.existsById(id)).thenReturn(true);

        restauranteGateway.existsById(id);

        verify(restauranteRepository, times(1)).existsById(id);
    }

    private RestauranteDomain buildRestauranteDomain(){
        return new RestauranteDomain(
                "Teste",
                "Localizacao",
                "tipoCozinha",
                List.of(LocalTime.of(10, 0, 0, 0)),
                2
        );
    }

    private Query buildQuery(String nome, String tipoCozinha, String localizacao){
        Query query = new Query();

        if(!ObjectUtils.isEmpty(nome)){
            Criteria criteria = Criteria.where("nome").in(nome);
            query.addCriteria(criteria);
        }
        if(!ObjectUtils.isEmpty(tipoCozinha)){
            Criteria criteria = Criteria.where("tipoCozinha").in(tipoCozinha);
            query.addCriteria(criteria);
        }
        if(!ObjectUtils.isEmpty(localizacao)){
            Criteria criteria = Criteria.where("localizacao").in(localizacao);
            query.addCriteria(criteria);
        }

        return query;
    }
}
