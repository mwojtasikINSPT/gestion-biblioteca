package views;

import controllers.BibliotecarioController;
import dtos.BibliotecarioDTO;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BibliotecarioView {

    private final BibliotecarioController bibliotecarioController;
    private final Scanner scanner;

    public BibliotecarioView(Scanner scanner) {
        this.bibliotecarioController = new BibliotecarioController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE BIBLIOTECARIOS ---\n"
                    + "1. Agregar Bibliotecario\n"
                    + "2. Listar Bibliotecarios\n"
                    + "3. Buscar Bibliotecario por ID\n"
                    + "4. Modificar Bibliotecario\n"
                    + "5. Eliminar Bibliotecario\n"
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
        Mostrar.Titulo("Agregar Bibliotecario");

        String idGenerado = Validaciones.generarSiguienteId(bibliotecarioController.listarBibliotecarios().stream().map(BibliotecarioDTO::getId).collect(Collectors.toList()),
                bibliotecarioController.obtenerIdsHistoricos(),
                "B");

        mostrarTexto("ID asignado automáticamente: " + idGenerado);

        boolean dniRepetido;

        mostrarTexto(Mensajes.PEDIR_DATO + "DNI (8 dígitos): ");
        String dni = scanner.nextLine();
        dniRepetido = bibliotecarioController.existeDni(dni);

        if (!Validaciones.esDniValido(dni)) {
            mostrarTexto(Mensajes.ERROR_DATO);
            return;
        } else if (dniRepetido) {
            mostrarTexto(Mensajes.DATO_DUPLICADO);
            return;
        }

        String nombre;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Nombre: ");
            nombre = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(nombre)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Apellido: ");
            apellido = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(apellido)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        try {
            bibliotecarioController.agregarBibliotecario(idGenerado, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Bibliotecarios");
        List<BibliotecarioDTO> bibliotecarios = bibliotecarioController.listarBibliotecarios();

        if (bibliotecarios.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (BibliotecarioDTO b : bibliotecarios) {
            mostrarTexto("ID: " + b.getId() + " | DNI: " + b.getDni() + " | " + b.getNombre() + " " + b.getApellido());
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Bibliotecario");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            BibliotecarioDTO b = bibliotecarioController.buscarBibliotecarioPorId(id);
            if (b != null) {
                mostrarTexto("Encontrado -> ID: " + b.getId() + " | DNI: " + b.getDni() + " | " + b.getNombre() + " " + b.getApellido());
            } else {
                Mostrar.ErrorNoEncontrado("Bibliotecario", id);
            }
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Bibliotecario");
        
        if (!bibliotecarioController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }
        
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario a modificar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        BibliotecarioDTO existente;
        try {
            existente = bibliotecarioController.buscarBibliotecarioPorId(id);
        } catch (IllegalArgumentException e) {
            Mostrar.ErrorNoEncontrado("Bibliotecario", id);
            return;
        }
        
        if (existente == null) {
            Mostrar.ErrorNoEncontrado("Bibliotecario", id);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_NUEVOS_DATOS);

        String dni;
        boolean dniRepetido;

        do {
            mostrarTexto("Nuevo DNI (8 dígitos): ");
            dni = scanner.nextLine();
            dniRepetido = bibliotecarioController.existeDni(dni);
            
            if (!dni.equals(existente.getDni()) && dniRepetido) {
                mostrarTexto("Ya existe un bibliotecario registrado con ese DNI.");
                dniRepetido = true;
            } else {
                dniRepetido = false;
            }

            if (!Validaciones.esDniValido(dni)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esDniValido(dni) || dniRepetido);

        String nombre;
        do {
            mostrarTexto("Nuevo Nombre: ");
            nombre = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(nombre)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            mostrarTexto("Nuevo Apellido: ");
            apellido = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(apellido)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        try {
            bibliotecarioController.modificarBibliotecario(id, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Bibliotecario");

        if (bibliotecarioController.listarBibliotecarios().isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario a eliminar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            bibliotecarioController.eliminarBibliotecario(id);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}