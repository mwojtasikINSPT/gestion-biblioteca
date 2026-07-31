package gestionbibliotecaria;

import static gestionbibliotecaria.AppConfig.iniciarSistema;


public class Main {

    public static void main(String[] args) {
         //Configuro codificación del sistema con UTF-8
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        iniciarSistema();
    }

}
