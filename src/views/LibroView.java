package views;

import controllers.LibroController;
import dtos.LibroDTO;
import models.Estado;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LibroView {

    private final LibroController libroController;
    private final Scanner scanner;

    public LibroView(Scanner scanner) {
        this.libroController = new LibroController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE LIBROS ---\n"
                    + "1. Agregar Libro\n"
                    + "2. Listar Libros\n"
                    + "3. Buscar Libro por ID\n"
                    + "4. Modificar Libro\n"
                    + "5. Eliminar Libro\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    agregar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    buscarPorId();
                    break;
                case 4:
                    modificar();
                    break;
                case 5:
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
        System.out.println("\n---> " + texto);
    }

    private void agregar() {
        Mostrar.Titulo("Agregar Libro");

        String idGenerado = Validaciones.generarSiguienteId(
                libroController.listarLibros().stream().map(LibroDTO::getId).collect(Collectors.toList()),
                libroController.obtenerIdsHistoricos(),
                "L");

        mostrarTexto("ID asignado automáticamente: " + idGenerado);

        String titulo;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Título: ");
            titulo = scanner.nextLine().trim();
            if (titulo.isEmpty()) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (titulo.isEmpty());

        String autor;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Autor: ");
            autor = scanner.nextLine().trim();
            if (autor.isEmpty()) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (autor.isEmpty());

        Estado estado = Estado.DISPONIBLE;
        try {
            libroController.agregarLibro(idGenerado, titulo, autor, estado);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Libros");
        List<LibroDTO> libros = libroController.listarLibros();

        if (libros.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (LibroDTO l : libros) {
            mostrarTexto("ID: " + l.getId() + " | Título: " + l.getTitulo() + " | Autor: " + l.getAutor() + " | Estado: " + l.getEstado());
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Libro");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del libro: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            LibroDTO l = libroController.buscarLibroPorId(id);
            mostrarTexto("Encontrado -> ID: " + l.getId() + " | Título: " + l.getTitulo() + " | Autor: " + l.getAutor() + " | Estado: " + l.getEstado());

        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Libro");

        if (!libroController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del libro a modificar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            LibroDTO existente = libroController.buscarLibroPorId(id);

            mostrarTexto(Mensajes.PEDIR_NUEVOS_DATOS);

            String titulo;
            do {
                mostrarTexto("Nuevo Título: ");
                titulo = scanner.nextLine().trim();
                if (titulo.isEmpty()) {
                    mostrarTexto(Mensajes.ERROR_DATO);
                }
            } while (titulo.isEmpty());

            String autor;
            do {
                mostrarTexto("Nuevo Autor: ");
                autor = scanner.nextLine().trim();
                if (autor.isEmpty()) {
                    mostrarTexto(Mensajes.ERROR_DATO);
                }
            } while (autor.isEmpty());

            Estado estado = null;
            do {
                mostrarTexto("Nuevo Estado (DISPONIBLE, PRESTADO): ");
                String estadoStr = Validaciones.normalizarTexto(scanner.nextLine());
                try {
                    estado = Estado.valueOf(estadoStr.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    mostrarTexto(Mensajes.ERROR_DATO);
                }
            } while (estado == null);

            libroController.modificarLibro(id, titulo, autor, estado);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Libro");

        if (libroController.listarLibros().isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del libro a eliminar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            libroController.eliminarLibro(id);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
