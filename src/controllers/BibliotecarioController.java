package controllers;

import daos.BibliotecarioDAO;
import dtos.BibliotecarioDTO;
import java.util.List;

public class BibliotecarioController {

    private final BibliotecarioDAO bibliotecarioDAO;

    public BibliotecarioController() {
        this.bibliotecarioDAO = new BibliotecarioDAO();
    }

    public String obtenerSiguienteId() {
        List<String> idsExistentes = bibliotecarioDAO.obtenerIdsHistoricos();
        return utils.Validaciones.generarSiguienteId(idsExistentes, "B");
    }

    public void agregarBibliotecario(String id, String dni, String nombre, String apellido) {
        BibliotecarioDTO bibliotecarioDTO = new BibliotecarioDTO(id, dni, nombre, apellido);
        boolean agregado = bibliotecarioDAO.agregar(bibliotecarioDTO);
        if (!agregado) {
            throw new IllegalArgumentException("Ya existe un bibliotecario con el ID: " + id);
        }
    }

    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return bibliotecarioDAO.hayRegistros();
    }

    public List<BibliotecarioDTO> listarBibliotecarios() {
        return bibliotecarioDAO.obtenerRegistros();
    }

    public BibliotecarioDTO buscarBibliotecarioPorId(String id) {
        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(id);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("No se encontró un bibliotecario con el ID: " + id);
        }
        return bibliotecario;
    }

    public void modificarBibliotecario(String id, String dni, String nombre, String apellido) {
        BibliotecarioDTO bibliotecarioDTO = new BibliotecarioDTO(id, dni, nombre, apellido);
        boolean modificado = bibliotecarioDAO.modificar(bibliotecarioDTO);
        if (!modificado) {
            throw new IllegalArgumentException("No se puede modificar. No existe un bibliotecario con el ID: " + id);
        }
    }

    public void eliminarBibliotecario(String id) {
        boolean eliminado = bibliotecarioDAO.eliminar(id);
        if (!eliminado) {
            throw new IllegalArgumentException("No se puede eliminar. No existe un bibliotecario con el ID: " + id);
        }
    }

    public boolean existeDni(String dni) {
        for (BibliotecarioDTO b : listarBibliotecarios()) {
            if (b.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    public List<String> obtenerIdsHistoricos() {
        return bibliotecarioDAO.obtenerIdsHistoricos();
    }
}
