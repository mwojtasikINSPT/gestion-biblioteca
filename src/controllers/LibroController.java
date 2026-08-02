package controllers;

import daos.LibroDAO;
import daos.PrestamoDAO;
import dtos.LibroDTO;
import java.util.ArrayList;
import models.Estado;
import utils.Mensajes;
import java.util.List;
import models.Libro;
import models.Prestamo;

public class LibroController {

    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;

    public LibroController() {
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    public void agregarLibro(String id, String titulo, String autor, Estado estado) {
        Libro libro = new Libro(id, titulo, autor, estado);

        boolean agregado = libroDAO.agregar(libro);
        if (!agregado) {
            throw new IllegalArgumentException("Ya existe un libro con el ID: " + id);
        }
    }

    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return libroDAO.hayRegistros();
    }

    public List<LibroDTO> listarLibros() {
        List<Libro> libros = libroDAO.obtenerRegistros();
        List<LibroDTO> listaDTO = new ArrayList<>();

        // Convierto de Modelo a DTO
        for (Libro libro : libros) {
            listaDTO.add(new LibroDTO(libro.getId(), libro.getTitulo(), libro.getAutor(), libro.getEstado()));
        }
        return listaDTO;
    }

    public LibroDTO buscarLibroPorId(String id) {
        Libro libro = libroDAO.obtenerPorId(id);
        if (libro == null) {
            throw new IllegalArgumentException("No se encontró un libro con el ID: " + id);
        }
        // Traducimos el Modelo encontrado a un DTO para la Vista
        return new LibroDTO(libro.getId(), libro.getTitulo(), libro.getAutor(), libro.getEstado());
    }

    public void modificarLibro(String id, String titulo, String autor, Estado estado) {
        Libro libroModificado = new Libro(id, titulo, autor, estado);

        boolean modificado = libroDAO.modificar(libroModificado);
        if (!modificado) {
            throw new IllegalArgumentException("No se puede modificar. No existe un libro con el ID: " + id);
        }
    }

    public void eliminarLibro(String id) {
        Prestamo prestamo = prestamoDAO.obtenerPrestamoPorLibro(id);
        if (prestamo != null) {
            throw new IllegalArgumentException(Mensajes.ERROR_ELIMINAR_EN_USO);
        }

        boolean eliminado = libroDAO.eliminar(id);
        if (!eliminado) {
            throw new IllegalArgumentException("No se puede eliminar. No existe un libro con el ID: " + id);
        }
    }

    public List<String> obtenerIdsHistoricos() {
        return libroDAO.obtenerIdsHistoricos();
    }
}
