package br.com.restauranteadjt.infrastructure.persistence.collection;

import br.com.restauranteadjt.infrastructure.persistence.valueObjects.AvaliacaoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "Restaurante")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteCollection {

    @Id
    private String id;
    private String nome;
    private String localizacao;
    private String tipoCozinha;
    private List<LocalTime> horariosFuncionamento;
    private Integer capacidade;
    private List<AvaliacaoVO> avaliacoes;

    public RestauranteCollection(String id, String nome, String localizacao, String tipoCozinha,
                                 List<LocalTime> horariosFuncionamento, Integer capacidade) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.tipoCozinha = tipoCozinha;
        this.horariosFuncionamento = horariosFuncionamento;
        this.capacidade = capacidade;
    }

    public RestauranteCollection(String nome, String localizacao, String tipoCozinha,
                                 List<LocalTime> horariosFuncionamento, Integer capacidade) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.tipoCozinha = tipoCozinha;
        this.horariosFuncionamento = horariosFuncionamento;
        this.capacidade = capacidade;
    }

}
