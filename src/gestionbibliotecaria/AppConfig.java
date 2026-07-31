package gestionbibliotecaria;

import views.LibroView;
import views.BibliotecarioView;
import views.LectorView;
import views.PrestamoView;
import views.AsignacionView;
import views.ConsultasView;
import utils.Mensajes;
import utils.Mostrar;
import java.util.Scanner;

public class AppConfig {

    public static void iniciarSistema() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        LectorView lectorView = new LectorView(scanner);
        BibliotecarioView bibliotecarioView = new BibliotecarioView(scanner);
        LibroView libroView = new LibroView(scanner);
        PrestamoView prestamoView = new PrestamoView(scanner);
        AsignacionView asignacionView = new AsignacionView(scanner);
        ConsultasView consultasView = new ConsultasView(scanner);

        do {
            String menuPrincipal = """
                                   
                                   --- SISTEMA DE GESTION BIBLIOTECARIA ---
                                   1. Gestión de Lectores
                                   2. Gestión de Bibliotecarios
                                   3. Gestión de Libros
                                   4. Gestión de Préstamos
                                   5. Gestión de Asignaciones (Bibliotecario - Lector)
                                   6. Búsquedas
                                   0. Salir""";

            opcion = Mostrar.Menu(menuPrincipal, scanner);

            switch (opcion) {
                case 1:
                    lectorView.mostrarMenu();
                    break;
                case 2:
                    bibliotecarioView.mostrarMenu();
                    break;
                case 3:
                    libroView.mostrarMenu();
                    break;
                case 4:
                    prestamoView.mostrarMenu();
                    break;
                case 5:
                    asignacionView.mostrarMenu();
                    break;
                case 6:
                    consultasView.mostrarMenu();
                    break;
                case 0:
                    Mostrar.Mensaje(Mensajes.SALIENDO);
                    break;
                case -1:
                default:
                    Mostrar.Mensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);

        scanner.close();
    }
}