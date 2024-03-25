package br.com.restauranteadjt.main.setup;

import br.com.restauranteadjt.infrastructure.persistence.collection.RestauranteCollection;
import br.com.restauranteadjt.infrastructure.persistence.repository.RestauranteRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class SetupDataBase {
    private final RestauranteRepository restauranteRepository;

    public SetupDataBase(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void warmup() {
        if(!restauranteRepository.existsById("660163012caaff5160053f8a")) {
            restauranteRepository.save(buildRestaurante());
        }
    }

    private RestauranteCollection buildRestaurante(){
        return new RestauranteCollection(
                "660163012caaff5160053f8a",
                "Paris 6",
                "Rua Haddock Lobo",
                "Bistrô",
                List.of(LocalTime.of(12, 0, 0)),
                600
                );
    }
}
