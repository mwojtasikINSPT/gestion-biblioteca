package views;

import controllers.AsignacionController;
import controllers.BibliotecarioController;
import controllers.LectorController;
import dtos.AsignacionDTO;
import dtos.BibliotecarioDTO;
import dtos.LectorDTO;
import utils.Mensajes;
import utils.Mostrar;
import java.util.Scanner;
import utils.Validaciones;

public class AsignacionView {

    private final AsignacionController asignacionController;
    private final Scanner scanner;

    public AsignacionView(Scanner scanner) {
        this.asignacionController = new AsignacionController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE ASIGNACIONES (BIBLIOTECARIO - LECTOR) ---\n"
                    + "1. Asignar Bibliotecario a Lector\n"
                    + "2. Listar Asignaciones\n"
                    + "3. Modificar Asignación\n"
                    + "4. Cancelar Asignación\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    asignar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    modificar();
                    break;
                case 4:
                    eliminar();
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
        System.out.println("\n" + texto);
    }

    private void asignar() {
        Mostrar.Titulo("Asignar Bibliotecario a Lector");

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario: ");
        String idBibliotecario = Validaciones.normalizarTexto(scanner.nextLine());

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector: ");
        String idLector = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            String idGenerado = asignacionController.asignarBibliotecarioALector(idBibliotecario, idLector);
            mostrarTexto("ID de Asignación generado: " + idGenerado);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Asignaciones");

        if (!asignacionController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        // Instanciamos los controladores necesarios si no los tenés globales
        LectorController lectorController = new LectorController();
        BibliotecarioController bibliotecarioController = new BibliotecarioController();

        for (AsignacionDTO a : asignacionController.listarAsignaciones()) {
            LectorDTO lector = lectorController.buscarLectorPorId(a.getIdLector());
            BibliotecarioDTO biblio = bibliotecarioController.buscarBibliotecarioPorId(a.getIdBibliotecario());

            String nombreLector = (lector != null) ? lector.getNombre() + " " + lector.getApellido() : "Lector no encontrado";
            String nombreBiblio = (biblio != null) ? biblio.getNombre() + " " + biblio.getApellido() : "Bibliotecario no encontrado";

            String detalle = "Asignación [" + a.getIdAsignacion() + "]:\n"
                    + "     Bibliotecario: [" + a.getIdBibliotecario() + "] " + nombreBiblio + "\n"
                    + "     Lector:        [" + a.getIdLector() + "] " + nombreLector;

            mostrarTexto(detalle);
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Asignación");

        if (!asignacionController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector de la asignación a modificar: ");
        String idLector = Validaciones.normalizarTexto(scanner.nextLine());

        mostrarTexto(Mensajes.PEDIR_DATO + "Nuevo ID del bibliotecario: ");
        String nuevoIdBibliotecario = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            asignacionController.modificarAsignacion(idLector, nuevoIdBibliotecario);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Asignación");

        if (!asignacionController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector de la asignación a cancelar: ");
        String idLector = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            asignacionController.eliminarAsignacion(idLector);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
