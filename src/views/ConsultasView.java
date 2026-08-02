package views;

import controllers.ConsultasController;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.Scanner;

public class ConsultasView {

    private final ConsultasController consultasController;
    private final Scanner scanner;

    public ConsultasView(Scanner scanner) {
        this.consultasController = new ConsultasController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- BÚSQUEDAS Y CONSULTAS ---\n"
                    + "1. Consultar el libro prestado a un lector\n"
                    + "2. Consultar los lectores atendidos por un bibliotecario\n"
                    + "3. Consultar el bibliotecario asignado a un lector\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    consultarLibroDeLector();
                    break;
                case 2:
                    consultarLectoresDeBibliotecario();
                    break;
                case 3:
                    consultarBibliotecarioDeLector();
                    break;
                case 0:
                    mostrarTexto(Mensajes.VOLVIENDO);
                    break;
                case -1:
                default:
                    mostrarTexto(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void mostrarTexto(String texto) {
        System.out.println("\n---> " + texto);
    }

    private void consultarLibroDeLector() {
        Mostrar.Titulo("Consultar Libro Prestado a un Lector");
        
        if (!consultasController.hayPrestamos()) {
            mostrarTexto("Actualmente no hay ningún préstamo registrado en el sistema.");
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector: ");
        String idLector = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            String resultado = consultasController.consultarLibroPrestadoALector(idLector);
            mostrarTexto("\n" + resultado);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void consultarLectoresDeBibliotecario() {
        Mostrar.Titulo("Consultar Lectores por Bibliotecario");
        
        if (!consultasController.hayAsignaciones()) {
            mostrarTexto("Actualmente no hay asignaciones registradas en el sistema.");
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario: ");
        String idBibliotecario = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            String resultado = consultasController.consultarLectoresPorBibliotecario(idBibliotecario);
            mostrarTexto("\n" + resultado);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void consultarBibliotecarioDeLector() {
        Mostrar.Titulo("Consultar Bibliotecario Asignado a un Lector");
        
        if (!consultasController.hayAsignaciones()) {
            mostrarTexto("Actualmente no hay asignaciones registradas en el sistema.");
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector: ");
        String idLector = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            String resultado = consultasController.consultarBibliotecarioDeLector(idLector);
            mostrarTexto("\n" + resultado);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}