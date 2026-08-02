package views;

import controllers.BibliotecarioController;
import dtos.BibliotecarioDTO;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import java.util.Scanner;

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

        // Muestro el ID generado de antemano
        String idGenerado = bibliotecarioController.obtenerSiguienteId();
        mostrarTexto("ID asignado automáticamente: " + idGenerado);

        // Pido y valido el DNI
        mostrarTexto(Mensajes.PEDIR_DATO + "DNI (8 dígitos): ");
        String dni = scanner.nextLine();

        if (!Validaciones.esDniValido(dni)) {
            mostrarTexto(Mensajes.ERROR_DATO);
            return;
        }

        if (bibliotecarioController.existeDni(dni)) {
            mostrarTexto(Mensajes.DATO_DUPLICADO);
            return;
        }

        // Pido y valido el nombre
        String nombre;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Nombre: ");
            nombre = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(nombre)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        // Pido y valido el apellido
        String apellido;
        do {
            mostrarTexto(Mensajes.PEDIR_DATO + "Apellido: ");
            apellido = Validaciones.normalizarTexto(scanner.nextLine());
            if (!Validaciones.esTextoValido(apellido)) {
                mostrarTexto(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        try {
            // Guardo pasando el ID que ya le mostré al usuario
            bibliotecarioController.agregarBibliotecario(idGenerado, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_GUARDAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void listar() {
        Mostrar.Titulo("Lista de Bibliotecarios");

        // Verifico si hay registros cargados antes de listar
        if (!bibliotecarioController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        // Recorro y muestro cada bibliotecario con su formato correspondiente
        for (BibliotecarioDTO b : bibliotecarioController.listarBibliotecarios()) {
            String detalle = "Bibliotecario [" + b.getId() + "]:\n"
                    + "     DNI:      " + b.getDni() + "\n"
                    + "     Nombre:   " + b.getNombre() + " " + b.getApellido();

            mostrarTexto(detalle);
        }
    }

    private void buscarPorId() {
        Mostrar.Titulo("Buscar Bibliotecario");
        
        // Pido el ID del bibliotecario
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            // Busco el bibliotecario (si no existe, el controlador ya lanza la excepción)
            BibliotecarioDTO b = bibliotecarioController.buscarBibliotecarioPorId(id);
            
            // Muestro los datos encontrados
            mostrarTexto("Encontrado -> ID: " + b.getId() + " | DNI: " + b.getDni() + " | " + b.getNombre() + " " + b.getApellido());
            
        } catch (IllegalArgumentException e) {
            // Muestro el mensaje de error capturado
            mostrarTexto(e.getMessage());
        }
    }

    private void modificar() {
        Mostrar.Titulo("Modificar Bibliotecario");

        // Verifico si hay registros cargados antes de continuar
        if (!bibliotecarioController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        // Pido el ID del bibliotecario a modificar
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario a modificar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            // Verifico que el bibliotecario exista antes de pedir los nuevos datos
            BibliotecarioDTO bibliotecarioActual = bibliotecarioController.buscarBibliotecarioPorId(id);

            // Pido y valido el nuevo DNI
            mostrarTexto(Mensajes.PEDIR_DATO + "Nuevo DNI (8 dígitos): ");
            String dni = scanner.nextLine();

            if (!Validaciones.esDniValido(dni)) {
                mostrarTexto(Mensajes.ERROR_DATO);
                return;
            }

            // Pido y valido el nuevo nombre
            String nombre;
            do {
                mostrarTexto(Mensajes.PEDIR_DATO + "Nuevo nombre: ");
                nombre = Validaciones.normalizarTexto(scanner.nextLine());
                if (!Validaciones.esTextoValido(nombre)) {
                    mostrarTexto(Mensajes.ERROR_DATO);
                }
            } while (!Validaciones.esTextoValido(nombre));

            // Pido y valido el nuevo apellido
            String apellido;
            do {
                mostrarTexto(Mensajes.PEDIR_DATO + "Nuevo apellido: ");
                apellido = Validaciones.normalizarTexto(scanner.nextLine());
                if (!Validaciones.esTextoValido(apellido)) {
                    mostrarTexto(Mensajes.ERROR_DATO);
                }
            } while (!Validaciones.esTextoValido(apellido));

            // Actualizo los datos del bibliotecario
            bibliotecarioController.modificarBibliotecario(id, dni, nombre, apellido);
            mostrarTexto(Mensajes.EXITO_ACTUALIZAR);

        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }

    private void eliminar() {
        Mostrar.Titulo("Eliminar Bibliotecario");

        // Verifico si hay registros cargados antes de continuar
        if (!bibliotecarioController.hayRegistros()) {
            mostrarTexto(Mensajes.SIN_REGISTROS);
            return;
        }

        // Pido el ID del bibliotecario que quiero eliminar
        mostrarTexto(Mensajes.PEDIR_DATO + "ID del bibliotecario a eliminar: ");
        String id = Validaciones.normalizarTexto(scanner.nextLine());

        try {
            // Elimino el bibliotecario por su ID
            bibliotecarioController.eliminarBibliotecario(id);
            mostrarTexto(Mensajes.EXITO_ELIMINAR);
        } catch (IllegalArgumentException e) {
            mostrarTexto(e.getMessage());
        }
    }
}
