package controllers;

import daos.LectorDAO;
import daos.PrestamoDAO;
import dtos.LectorDTO;
import utils.Mensajes;
import java.util.List;

public class LectorController {

    private final LectorDAO lectorDAO;
    private final PrestamoDAO prestamoDAO;

    public LectorController() {
        this.lectorDAO = new LectorDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    public void agregarLector(String id, String dni, String nombre, String apellido) {
        LectorDTO lectorDTO = new LectorDTO(id, dni, nombre, apellido);
        boolean agregado = lectorDAO.agregar(lectorDTO);
        if (!agregado) {
            throw new IllegalArgumentException("Ya existe un lector con el ID: " + id);
        }
    }

    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return lectorDAO.hayRegistros(); 
    }
    
    public List<LectorDTO> listarLectores() {
        return lectorDAO.obtenerRegistros();
    }

    public LectorDTO buscarLectorPorId(String id) {
        LectorDTO lector = lectorDAO.obtenerPorId(id);
        if (lector == null) {
            throw new IllegalArgumentException("No se encontró un lector con el ID: " + id);
        }
        return lector;
    }

    public void modificarLector(String id, String dni, String nombre, String apellido) {
        LectorDTO lectorDTO = new LectorDTO(id, dni, nombre, apellido);
        boolean modificado = lectorDAO.modificar(lectorDTO);
        if (!modificado) {
            throw new IllegalArgumentException("No se puede modificar. No existe un lector con el ID: " + id);
        }
    }

    public void eliminarLector(String id) {
        boolean tienePrestamoActivo = prestamoDAO.obtenerRegistros().stream()
                .anyMatch(p -> p.getIdLector().equalsIgnoreCase(id));

        if (tienePrestamoActivo) {
            throw new IllegalArgumentException(Mensajes.ERROR_ELIMINAR_EN_USO);
        }

        boolean eliminado = lectorDAO.eliminar(id);
        if (!eliminado) {
            throw new IllegalArgumentException("No se puede eliminar. No existe un lector con el ID: " + id);
        }
    }

    public boolean existeDni(String dni) {
        for (LectorDTO l : listarLectores()) {
            if (l.getDni().equals(dni)) {
                return true; 
            }
        }
        return false;
    }

    public List<String> obtenerIdsHistoricos() {
        return lectorDAO.obtenerIdsHistoricos();
    }
}