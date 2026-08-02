package controllers;

import daos.AsignacionDAO;
import daos.BibliotecarioDAO;
import daos.LectorDAO;
import dtos.AsignacionDTO;
import dtos.BibliotecarioDTO;
import utils.Mensajes;
import java.util.List;
import models.Lector;

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
        // Obtengo los modelos y preparo la lista de DTOs para la vista
        List<models.Asignacion> asignaciones = asignacionDAO.obtenerRegistros();
        List<AsignacionDTO> listaDTO = new java.util.ArrayList<>();

        for (models.Asignacion asignacion : asignaciones) {
            // Mapeo el modelo completo pasando los tres atributos al DTO
            listaDTO.add(new AsignacionDTO(asignacion.getIdAsignacion(), asignacion.getIdBibliotecario(), asignacion.getIdLector()));
        }

        return listaDTO;
    }

    public boolean hayRegistros() {
        // Llama al método que el DAO heredó automáticamente de la interfaz
        return asignacionDAO.hayRegistros();
    }

    public String asignarBibliotecarioALector(String idBibliotecario, String idLector) {
        // Valido la existencia del bibliotecario
        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(idBibliotecario);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("No existe un bibliotecario con el ID: " + idBibliotecario);
        }

        // Valido la existencia del lector
        Lector lector = lectorDAO.obtenerPorId(idLector);
        if (lector == null) {
            throw new IllegalArgumentException("No existe un lector con el ID: " + idLector);
        }

        // Obtengo los IDs históricos y genero el siguiente ID de asignación automáticamente
        List<String> idsExistentes = asignacionDAO.obtenerIdsHistoricos();
        String idAsignacion = utils.Validaciones.generarSiguienteId(idsExistentes, "A");

        // Instancio el modelo completo con el ID generado
        models.Asignacion nuevaAsignacion = new models.Asignacion(idAsignacion, idBibliotecario, idLector);
        boolean agregado = asignacionDAO.agregar(nuevaAsignacion);
        if (!agregado) {
            throw new IllegalArgumentException("El lector ya tiene un bibliotecario asignado o el ID ya existe.");
        }
        return idAsignacion;
    }

    public void modificarAsignacion(String idLector, String nuevoIdBibliotecario) {
        // Valido la existencia del nuevo bibliotecario
        BibliotecarioDTO bibliotecario = bibliotecarioDAO.obtenerPorId(nuevoIdBibliotecario);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("No existe un bibliotecario con el ID: " + nuevoIdBibliotecario);
        }

        // Busco la asignación actual asociada al lector
        models.Asignacion asignacionActual = asignacionDAO.obtenerPorLector(idLector);
        if (asignacionActual == null) {
            throw new IllegalArgumentException("El lector no tiene un bibliotecario asignado.");
        }

        // Creo el modelo actualizado manteniendo el ID original de la asignación
        models.Asignacion asignacionModificada = new models.Asignacion(
                asignacionActual.getIdAsignacion(),
                nuevoIdBibliotecario,
                idLector
        );

        boolean modificado = asignacionDAO.modificar(asignacionModificada);
        if (!modificado) {
            throw new IllegalArgumentException("No se pudo modificar la asignación en la base de datos.");
        }
    }

    public void eliminarAsignacion(String idLector) {
        // Busco la asignación asociada al lector para obtener su ID único
        models.Asignacion asignacion = asignacionDAO.obtenerPorLector(idLector);
        if (asignacion == null) {
            throw new IllegalArgumentException(Mensajes.ERROR_NO_ENCONTRADO);
        }

        // Elimino la asignación utilizando su ID único
        boolean eliminado = asignacionDAO.eliminar(asignacion.getIdAsignacion());
        if (!eliminado) {
            throw new IllegalArgumentException(Mensajes.ERROR_NO_ENCONTRADO);
        }
    }
}
