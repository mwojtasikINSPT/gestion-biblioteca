package views;

import controllers.LectorController;
import dtos.LectorDTO;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LectorView {

    private final LectorController lectorController;
    private final Scanner scanner;

    public LectorView(Scanner scanner) {
        this.lectorController = new LectorController();
        this.scanner = scanner;
    }

    public void mostrarMenu() {
        int opcion;
        do {
            String menuTexto = "\n--- GESTIÓN DE LECTORES ---\n"
                    + "1. Agregar Lector\n"
                    + "2. Listar Lectores\n"
                    + "3. Buscar Lector por ID\n"
                    + "4. Modificar Lector\n"
                    + "5. Eliminar Lector\n"
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
        System.out.println("\n--->" + texto);
    }

    private void agregar() {
        Mostrar.Titulo("Agregar Lector");

        String idGenerado = Validaciones.generarSiguienteId(lectorController.listarLectores().stream().map(LectorDTO::getId).collect(Collectors.toList()),
                lectorController.obtenerIdsHistoricos(),
                "LEC");
        mostrarTexto("ID asignado automáticamente: " + idGenerado);

        boolean dniRepetido;
        mostrarTexto(Mensajes.PEDIR_DATO + "DNI (8 dígitos): ");
        String dni = scanner.nextLine();
        dniRepetido = lectorController.existeDni(dni);

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
            lectorController.agregarLector(idGenerado, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Lectores");
        List<LectorDTO> lectores = lectorController.listarLectores();

        if (lectores.isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        for (LectorDTO l : lectores) {
            mostrarTexto("ID: " + l.getId() + " | DNI: " + l.getDni() + " | " + l.getNombre() + " " + l.getApellido());
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Lector");
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            // el controlador ya lanza excepción si no existe
            LectorDTO l = lectorController.buscarLectorPorId(id);
            mostrarTexto("Encontrado -> ID: " + l.getId() + " | DNI: " + l.getDni() + " | " + l.getNombre() + " " + l.getApellido());
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Lector");

        if (!lectorController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector a modificar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        LectorDTO existente;
        try {
            existente = lectorController.buscarLectorPorId(id);
        } catch (IllegalArgumentException e) {
            Mostrar.ErrorNoEncontrado("Lector", id);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_NUEVOS_DATOS);

        String dni;
        boolean dniRepetido;

        do {
            mostrarTexto("Nuevo DNI (8 dígitos): ");
            dni = scanner.nextLine();
            dniRepetido = lectorController.existeDni(dni);

            if (!dni.equals(existente.getDni()) && dniRepetido) {
                mostrarTexto("Ya existe un lector registrado con ese DNI.");
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
            lectorController.modificarLector(id, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Lector");

        if (lectorController.listarLectores().isEmpty()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        mostrarTexto(Mensajes.PEDIR_DATO + "ID del lector a eliminar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            lectorController.eliminarLector(id);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
