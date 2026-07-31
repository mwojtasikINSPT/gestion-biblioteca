package controllers;

import daos.LibroDAO;
import daos.PrestamoDAO;
import dtos.LibroDTO;
import dtos.PrestamoDTO;
import models.Estado;
import utils.Mensajes;
import java.util.List;

public class LibroController {
    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;

    public LibroController() {
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    public void agregarLibro(String id, String titulo, String autor, Estado estado) {
        LibroDTO libroDTO = new LibroDTO(id, titulo, autor, estado);
        
        boolean agregado = libroDAO.agregar(libroDTO);
        if (!agregado) {
            throw new IllegalArgumentException("Ya existe un libro con el ID: " + id);
        }
    }

    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return libroDAO.hayRegistros(); 
    }
    
    public List<LibroDTO> listarLibros() {
        return libroDAO.obtenerRegistros();
    }

    public LibroDTO buscarLibroPorId(String id) {
        LibroDTO libro = libroDAO.obtenerPorId(id);
        if (libro == null) {
            throw new IllegalArgumentException("No se encontró un libro con el ID: " + id);
        }
        return libro;
    }

    public void modificarLibro(String id, String titulo, String autor, Estado estado) {
        LibroDTO libroDTO = new LibroDTO(id, titulo, autor, estado);
        
        boolean modificado = libroDAO.modificar(libroDTO);
        if (!modificado) {
            throw new IllegalArgumentException("No se puede modificar. No existe un libro con el ID: " + id);
        }
    }

    public void eliminarLibro(String id) {
        PrestamoDTO prestamo = prestamoDAO.obtenerPorId(id);
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