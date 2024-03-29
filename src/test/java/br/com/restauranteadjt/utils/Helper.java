package br.com.restauranteadjt.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

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

    public static String criarTexto() {
        String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ";

        Random random = new Random();

        StringBuilder key = new StringBuilder();
        int index = -1;
        for( int i = 0; i < 9; i++ ) {
            index = random.nextInt( letras.length() );
            key.append(letras.charAt(index));
        }
        return key.toString();
    }
}
