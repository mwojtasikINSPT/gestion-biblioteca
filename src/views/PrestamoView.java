package views;

import controllers.LectorController;
import controllers.LibroController;
import controllers.PrestamoController;
import dtos.LectorDTO;
import dtos.LibroDTO;
import dtos.PrestamoDTO;
import utils.Mensajes;
import utils.Mostrar;
import java.util.Scanner;
import utils.Validaciones;

public class PrestamoView {

    private final PrestamoController prestamoController;
    private final Scanner scanner;

    public PrestamoView(Scanner scanner) {
        this.prestamoController = new PrestamoController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE PRÉSTAMOS ---\n"
                    + "1. Registrar Préstamo\n"
                    + "2. Listar Préstamos\n"
                    + "3. Cancelar Préstamo\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    registrarPrestamo();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    cancelarPrestamo();
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

    private void registrarPrestamo() {
        Mostrar.Titulo("Registrar Préstamo");

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del libro: ");
        String idLibro = Validaciones.normalizarTexto(scanner.nextLine());

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector: ");
        String idLector = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            prestamoController.registrarPrestamo(idLibro, idLector);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Préstamos");

        if (!prestamoController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        // Instanciamos los controladores necesarios si no los tenés globales
        LectorController lectorController = new LectorController();
        LibroController libroController = new LibroController();

        for (PrestamoDTO p : prestamoController.listarPrestamos()) {
            LectorDTO lector = lectorController.buscarLectorPorId(p.getIdLector());
            LibroDTO libro = libroController.buscarLibroPorId(p.getIdLibro());

            String nombreLector = (lector != null) ? lector.getNombre() + " " + lector.getApellido() : "Lector no encontrado";
            String tituloLibro = (libro != null) ? libro.getTitulo() : "Libro no encontrado";

            String detalle = "Préstamo:\n"
                    + "     Libro:  [" + p.getIdLibro() + "] " + tituloLibro + "\n"
                    + "     Lector: [" + p.getIdLector() + "] " + nombreLector;

            mostrarTexto(detalle);
        }
    }

    private void cancelarPrestamo() {
        Mostrar.Titulo("Cancelar Préstamo");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del libro del préstamo a cancelar: ");
        String idLibro = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            prestamoController.cancelarPrestamo(idLibro);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
