package views;

import controllers.ConsultasController;
import utils.Mensajes;
import utils.Mostrar;
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
            String menuTexto = "\n--- MENÚ DE CONSULTAS ---\n"
                    + "1. Ver pacientes con su cama asignada\n"
                    + "2. Ver pacientes atendidos por un médico\n"
                    + "3. Ver médico asignado a un paciente\n"
                    + "0. Volver al Menú Principal";

            opcion = Mostrar.Menu(menuTexto, scanner);

            switch (opcion) {
                case 1:
                    System.out.println("Pendiente...");
                    break;
                case 2:
                    System.out.println("Pendiente...");
                    break;
                case 3:
                    System.out.println("Pendiente...");
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

 
    }


