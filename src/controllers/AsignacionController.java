package controllers;

import daos.AsignacionDAO;
import daos.BibliotecarioDAO;
import daos.LectorDAO;
import dtos.AsignacionDTO;
import dtos.BibliotecarioDTO;
import dtos.LectorDTO;
import utils.Mensajes;
import java.util.List;

public class AsignacionController {
    private final AsignacionDAO asignacionDAO;
    private final BibliotecarioDAO bibliotecarioDAO;
    private final LectorDAO lectorDAO;

    public AsignacionController() {
        this.asignacionDAO = new AsignacionDAO();
        this.bibliotecarioDAO = new BibliotecarioDAO();
        this.lectorDAO = new LectorDAO();
    }

    public List<AsignacionDTO> listarAsignaciones() {
        return asignacionDAO.obtenerRegistros();
    }
    
    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return asignacionDAO.hayRegistros(); 
    }

    public void asignarBibliotecarioALector(String idBibliotecario, String idLector) {
        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(idBibliotecario);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("No existe un bibliotecario con el ID: " + idBibliotecario);
        }

        LectorDTO lector = lectorDAO.obtenerPorId(idLector);
        if (lector == null) {
            throw new IllegalArgumentException("No existe un lector con el ID: " + idLector);
        }

        AsignacionDTO asignacionDTO = new AsignacionDTO(idBibliotecario, idLector);
        boolean agregado = asignacionDAO.agregar(asignacionDTO);
        if (!agregado) {
            throw new IllegalArgumentException("El lector ya tiene un bibliotecario asignado.");
        }
    }

    public void modificarAsignacion(String idLector, String nuevoIdBibliotecario) {
        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(nuevoIdBibliotecario);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("No existe un bibliotecario con el ID: " + nuevoIdBibliotecario);
        }

        AsignacionDTO asignacionModificada = new AsignacionDTO(nuevoIdBibliotecario, idLector);
        boolean modificado = asignacionDAO.modificar(asignacionModificada);
        if (!modificado) {
            throw new IllegalArgumentException("El lector no tiene un bibliotecario asignado.");
        }
    }

    public void cancelarAsignacion(String idLector) {
        boolean eliminado = asignacionDAO.eliminar(idLector);
        if (!eliminado) {
            throw new IllegalArgumentException(Mensajes.ERROR_NO_ENCONTRADO);
        }
    }
}