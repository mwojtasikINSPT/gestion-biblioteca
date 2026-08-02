package controllers;

import daos.LectorDAO;
import daos.PrestamoDAO;
import dtos.LectorDTO;
import utils.Mensajes;
import java.util.ArrayList;
import java.util.List;
import models.Lector;

public class LectorController {

    private final LectorDAO lectorDAO;
    private final PrestamoDAO prestamoDAO;

    public LectorController() {
        this.lectorDAO = new LectorDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    public void agregarLector(String id, String dni, String nombre, String apellido) {
        // Instancio el modelo real para interactuar con la base de datos
        Lector lector = new Lector(id, dni, nombre, apellido);
        boolean agregado = lectorDAO.agregar(lector);
        if (!agregado) {
            throw new IllegalArgumentException("Ya existe un lector con el ID: " + id);
        }
    }

    public boolean hayRegistros() {
        return lectorDAO.hayRegistros();
    }

    public List<LectorDTO> listarLectores() {
        // Obtengo los modelos y preparo la lista de DTOs para la vista
        List<Lector> lectores = lectorDAO.obtenerRegistros();
        List<LectorDTO> listaDTO = new ArrayList<>();

        for (Lector lector : lectores) {
            // Copio los datos al DTO
            listaDTO.add(new LectorDTO(lector.getId(), lector.getDni(), lector.getNombre(), lector.getApellido()));
        }

        return listaDTO;
    }

    public LectorDTO buscarLectorPorId(String id) {
        // Busco la entidad original en la capa de datos
        Lector lector = lectorDAO.obtenerPorId(id);

        // Valido la existencia del registro
        if (lector == null) {
            throw new IllegalArgumentException("No se encontró un lector con el ID: " + id);
        }

        // Convierto el modelo en DTO y lo retorno
        return new LectorDTO(lector.getId(), lector.getDni(), lector.getNombre(), lector.getApellido());
    }

    public void modificarLector(String id, String dni, String nombre, String apellido) {
        // Construyo la entidad para enviar a persistencia
        Lector lector = new Lector(id, dni, nombre, apellido);
        boolean modificado = lectorDAO.modificar(lector);
        if (!modificado) {
            throw new IllegalArgumentException("No se puede modificar. No existe un lector con el ID: " + id);
        }
    }

    public void eliminarLector(String id) {
        // Verifico cruce de datos con préstamos antes de eliminar
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
        // Obtengo los registros directamente de la capa de datos
        for (Lector lector : lectorDAO.obtenerRegistros()) {
            // Verifico coincidencia exacta de DNI
            if (lector.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    public List<String> obtenerIdsHistoricos() {
        // Retorno la lista de Ids históricos, no requiere mapeo por ser un tipo de dato primitivo
        return lectorDAO.obtenerIdsHistoricos();
    }
}
