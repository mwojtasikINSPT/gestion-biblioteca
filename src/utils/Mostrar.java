package utils;

import java.util.Scanner;

public class Mostrar {

    // Imprime un menu multilinea y devuelve la opcion elegida
    public static int Menu(String mensajeMenu, Scanner scanner) {
        System.out.println(mensajeMenu);
        System.out.print("Opcion: ");

        try {
            // Leer la linea como texto
            String entrada = scanner.nextLine();
            // Convertir a nro
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            // Si ingresa valor distinto a nro, ataja error, Devuelvo un nro no valido
            return -1;
        }
    }

    // Imprime mensaje
    public static void Mensaje(String mensaje) {
        System.out.println(" " + mensaje);
    }

    public static void Titulo(String titulo) {
        System.out.println("\n---- " + titulo.toUpperCase() + " ----");
    }

    public static void ErrorNoEncontrado(String tipoEntidad, String id) {
        System.out.println("Error: " + tipoEntidad + "con Id/Codigo " + id + " no existe en sistema.");
    }

    public static void ErrorOcupado(String entidad, String id, String motivo) {
        System.out.println("Error de asignacion: " + entidad + "con Id/Codigo " + id + " ya " + motivo);
    }
}
