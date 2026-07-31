package controllers;

import daos.LibroDAO;
import daos.LectorDAO;
import daos.PrestamoDAO;
import dtos.LibroDTO;
import dtos.LectorDTO;
import dtos.PrestamoDTO;
import models.Estado;
import java.util.List;

public class PrestamoController {
    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;
    private final LectorDAO lectorDAO;

    public PrestamoController() {
        this.prestamoDAO = new PrestamoDAO();
        this.libroDAO = new LibroDAO();
        this.lectorDAO = new LectorDAO();
    }

    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return prestamoDAO.hayRegistros(); 
    }
    
    public List<PrestamoDTO> listarPrestamos() {
        return prestamoDAO.obtenerRegistros();
    }

    public void registrarPrestamo(String codigoLibro, String idLector) {
        LibroDTO libro = libroDAO.obtenerPorId(codigoLibro);
        if (libro == null) {
            throw new IllegalArgumentException("No existe un libro con el código: " + codigoLibro);
        }

        if (libro.getEstado() != Estado.DISPONIBLE) {
            throw new IllegalArgumentException("El libro seleccionado no se encuentra disponible (Estado actual: " + libro.getEstado() + ")");
        }

        LectorDTO lector = lectorDAO.obtenerPorId(idLector);
        if (lector == null) {
            throw new IllegalArgumentException("No existe un lector con el ID: " + idLector);
        }

        boolean lectorYaTienePrestamo = prestamoDAO.obtenerRegistros().stream()
                .anyMatch(p -> p.getIdLector().equalsIgnoreCase(idLector));
        if (lectorYaTienePrestamo) {
            throw new IllegalArgumentException("El lector ya tiene un libro prestado.");
        }

        PrestamoDTO prestamoDTO = new PrestamoDTO(codigoLibro, idLector);
        boolean agregado = prestamoDAO.agregar(prestamoDTO);
        if (!agregado) {
            throw new IllegalArgumentException("El sistema rechazó el registro. Ya existe un préstamo para este libro.");
        }

        libro.setEstado(Estado.PRESTADO);
        libroDAO.modificar(libro);
    }

    public void cancelarPrestamo(String codigoLibro) {
        PrestamoDTO prestamo = prestamoDAO.obtenerPorId(codigoLibro);
        if (prestamo == null) {
            throw new IllegalArgumentException("No existe un préstamo activo para el libro con código: " + codigoLibro);
        }

        boolean eliminado = prestamoDAO.eliminar(codigoLibro);
        if (!eliminado) {
            throw new IllegalArgumentException("No se pudo cancelar el préstamo en la base de datos.");
        }

        LibroDTO libro = libroDAO.obtenerPorId(codigoLibro);
        if (libro != null) {
            libro.setEstado(Estado.DISPONIBLE);
            libroDAO.modificar(libro);
        }
    }

    public boolean verificarLibroEnUso(String codigoLibro) {
        PrestamoDTO prestamo = prestamoDAO.obtenerPorId(codigoLibro);
        return prestamo != null;
    }
}