package br.com.restauranteadjt.bdd;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class StepDefinitionMesa {

    @Dado("que uma reserva foi efetuada")
    public void que_uma_reserva_foi_efetuada() {
    }
    @Quando("realizar a busca da mesa")
    public void realizar_a_busca_da_mesa() {
    }
    @Então("a mesa deve ser apresentado com sucesso")
    public void a_mesa_deve_ser_apresentado_com_sucesso() {
    }


    @Dado("que uma reserva foi efetuada e finalizada")
    public void que_uma_reserva_foi_efetuada_e_finalizada() {
    }
    @Então("deve atualizar status da mesa")
    public void deve_atualizar_status_da_mesa() {
    }
    @Então("retornar a mesa com status atualizado com sucesso")
    public void retornar_a_mesa_com_status_atualizado_com_sucesso() {
    }
}
