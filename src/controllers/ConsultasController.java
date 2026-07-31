package controllers;

import daos.AsignacionDAO;
import daos.BibliotecarioDAO;
import daos.LectorDAO;
import daos.LibroDAO;
import daos.PrestamoDAO;
import dtos.AsignacionDTO;
import dtos.BibliotecarioDTO;
import dtos.LectorDTO;
import dtos.LibroDTO;
import dtos.PrestamoDTO;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultasController {

    private final LibroDAO libroDAO;
    private final LectorDAO lectorDAO;
    private final BibliotecarioDAO bibliotecarioDAO;
    private final PrestamoDAO prestamoDAO;
    private final AsignacionDAO asignacionDAO;

    public ConsultasController() {
        this.libroDAO = new LibroDAO();
        this.lectorDAO = new LectorDAO();
        this.bibliotecarioDAO = new BibliotecarioDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.asignacionDAO = new AsignacionDAO();
    }

    // 1. Consultar el libro prestado a un lector
    public String consultarLibroPrestadoALector(String idLector) {
        LectorDTO lector = lectorDAO.obtenerPorId(idLector);
        if (lector == null) {
            throw new IllegalArgumentException("No existe un lector con el ID: " + idLector);
        }

        // Buscar préstamos activos del lector
        List<PrestamoDTO> prestamos = prestamoDAO.obtenerRegistros().stream()
                .filter(p -> p.getIdLector().equalsIgnoreCase(idLector))
                .collect(Collectors.toList());

        if (prestamos.isEmpty()) {
            throw new IllegalArgumentException("El lector " + lector.getNombre() + " " + lector.getApellido() + " no tiene libros prestados actualmente.");
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append("Datos del Lector: ").append(lector.getNombre()).append(" ").append(lector.getApellido())
                .append(" (DNI: ").append(lector.getDni()).append(")\n");

        resultado.append("Libro(s) prestado(s):\n");
        for (PrestamoDTO p : prestamos) {
            LibroDTO libro = libroDAO.obtenerPorId(p.getIdLibro());
            if (libro != null) {
                resultado.append(" - [ID: ").append(libro.getId()).append("] ").append(libro.getTitulo())
                        .append(" (Autor: ").append(libro.getAutor()).append(")\n");
            }
        }
        return resultado.toString();
    }

    // 2. Consultar los lectores atendidos por un bibliotecario
    public String consultarLectoresPorBibliotecario(String idBibliotecario) {
        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(idBibliotecario);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("No existe un bibliotecario con el ID: " + idBibliotecario);
        }

        List<AsignacionDTO> asignaciones = asignacionDAO.obtenerRegistros().stream()
                .filter(a -> a.getIdBibliotecario().equalsIgnoreCase(idBibliotecario))
                .collect(Collectors.toList());

        if (asignaciones.isEmpty()) {
            throw new IllegalArgumentException("El bibliotecario " + bibliotecario.getNombre() + " no tiene lectores asignados.");
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append("Datos del Bibliotecario: ").append(bibliotecario.getNombre()).append(" ").append(bibliotecario.getApellido()).append("\n");
        resultado.append("Lectores asignados:\n");

        for (AsignacionDTO a : asignaciones) {
            LectorDTO lector = lectorDAO.obtenerPorId(a.getIdLector());
            if (lector != null) {
                resultado.append(" - [ID: ").append(lector.getId()).append("] ").append(lector.getNombre())
                        .append(" ").append(lector.getApellido()).append(" (DNI: ").append(lector.getDni()).append(")\n");
            }
        }
        return resultado.toString();
    }

    // 3. Consultar el bibliotecario asignado a un lector
    public String consultarBibliotecarioDeLector(String idLector) {
        LectorDTO lector = lectorDAO.obtenerPorId(idLector);
        if (lector == null) {
            throw new IllegalArgumentException("No existe un lector con el ID: " + idLector);
        }

        AsignacionDTO asignacion = asignacionDAO.obtenerRegistros().stream()
                .filter(a -> a.getIdLector().equalsIgnoreCase(idLector))
                .findFirst()
                .orElse(null);

        if (asignacion == null) {
            throw new IllegalArgumentException("El lector " + lector.getNombre() + " no tiene ningún bibliotecario asignado.");
        }

        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(asignacion.getIdBibliotecario());

        return "Datos del Lector: " + lector.getNombre() + " " + lector.getApellido() + " (DNI: " + lector.getDni() + ")\n"
                + "Bibliotecario Asignado: " + bibliotecario.getNombre() + " " + bibliotecario.getApellido() + " [ID: " + bibliotecario.getId() + "]";
    }

    // Validaciones para la vista
    public boolean hayPrestamos() {
        return prestamoDAO.hayRegistros();
    }

    public boolean hayAsignaciones() {
        return asignacionDAO.hayRegistros();
    }
}
