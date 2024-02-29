package br.com.restauranteadjt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteDomain {
        private String id;
        private String nome;
        private String localizacao;
        private String tipoCozinha;
        private List<LocalTime> horariosFuncionamento;
        private Integer capacidade;

        public RestauranteDomain(String nome, String localizacao, String tipoCozinha, List<LocalTime> horariosFuncionamento, Integer capacidade) {
                this.nome = nome;
                this.localizacao = localizacao;
                this.tipoCozinha = tipoCozinha;
                this.horariosFuncionamento = horariosFuncionamento;
                this.capacidade = capacidade;
        }
}
