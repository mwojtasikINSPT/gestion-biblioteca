package controllers;

import daos.LibroDAO;
import daos.LectorDAO;
import daos.PrestamoDAO;
import dtos.PrestamoDTO;
import java.util.ArrayList;
import models.Estado;
import java.util.List;
import models.Prestamo;
import models.Libro;
import models.Lector;

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
        return prestamoDAO.hayRegistros();
    }

    public List<PrestamoDTO> listarPrestamos() {
        // Obtengo los modelos de la base de datos
        List<Prestamo> prestamos = prestamoDAO.obtenerRegistros();
        List<PrestamoDTO> listaDTO = new ArrayList<>();

        // Copio los datos al DTO para la vista
        for (Prestamo prestamo : prestamos) {
            PrestamoDTO dto = new PrestamoDTO(prestamo.getId(), prestamo.getIdLibro(), prestamo.getIdLector());
            listaDTO.add(dto);
        }

        return listaDTO;
    }

    public void registrarPrestamo(String idPrestamo, String codigoLibro, String idLector) {
        // Busco la entidad original en la capa de datos
        Libro libro = libroDAO.obtenerPorId(codigoLibro);
        if (libro == null) {
            throw new IllegalArgumentException("No existe un libro con el código: " + codigoLibro);
        }

        if (libro.getEstado() != Estado.DISPONIBLE) {
            throw new IllegalArgumentException("El libro seleccionado no se encuentra disponible (Estado actual: " + libro.getEstado() + ")");
        }

        // Busco la entidad original en la capa de datos
        Lector lector = lectorDAO.obtenerPorId(idLector);
        if (lector == null) {
            throw new IllegalArgumentException("No existe un lector con el ID: " + idLector);
        }

        boolean lectorYaTienePrestamo = prestamoDAO.obtenerRegistros().stream()
                .anyMatch(p -> p.getIdLector().equalsIgnoreCase(idLector));
        if (lectorYaTienePrestamo) {
            throw new IllegalArgumentException("El lector ya tiene un libro prestado.");
        }

        // Instancio el modelo para enviarlo a persistencia
        Prestamo nuevoPrestamo = new Prestamo(idPrestamo, codigoLibro, idLector);
        boolean agregado = prestamoDAO.agregar(nuevoPrestamo);
        if (!agregado) {
            throw new IllegalArgumentException("Error en el sistema: El ID de préstamo generado ya existe.");
        }

        libro.setEstado(Estado.PRESTADO);
        libroDAO.modificar(libro);
    }

    public void cancelarPrestamo(String codigoLibro) {
        // Obtengo el préstamo activo verificando el código del libro
        Prestamo prestamo = prestamoDAO.obtenerPrestamoPorLibro(codigoLibro);
        if (prestamo == null) {
            throw new IllegalArgumentException("No existe un préstamo activo para el libro con código: " + codigoLibro);
        }

        boolean eliminado = prestamoDAO.eliminar(prestamo.getId());
        if (!eliminado) {
            throw new IllegalArgumentException("No se pudo cancelar el préstamo en la base de datos.");
        }

        // Busco la entidad original para actualizar su estado
        Libro libro = libroDAO.obtenerPorId(codigoLibro);
        if (libro != null) {
            libro.setEstado(Estado.DISPONIBLE);
            libroDAO.modificar(libro);
        }
    }

    public boolean verificarLibroEnUso(String codigoLibro) {
        // Verifico existencia de préstamo para un libro específico
        Prestamo prestamo = prestamoDAO.obtenerPrestamoPorLibro(codigoLibro);
        return prestamo != null;
    }

    public List<String> obtenerIdsHistoricos() {
        // Retorno la lista de Ids históricos directamente de la capa de datos
        return prestamoDAO.obtenerIdsHistoricos();
    }
}