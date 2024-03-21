package br.com.restauranteadjt.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Helper {
    public static String criarIdRestaurante(){
        return "65fb9c5409096668df2fd2ee";
    }

    public static LocalTime criarHoraReserva() {
        return LocalTime.of(10, 0, 0, 0);
    }

    public static LocalDate criarDataReserva(){
        return  LocalDate.now().plusDays(1);
    }
}
