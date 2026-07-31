package daos;

import dtos.AsignacionDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO implements ICrud<AsignacionDTO, String> {

    private final String ARCHIVO = "asignaciones.txt";

    @Override
    public List<AsignacionDTO> obtenerRegistros() {
        List<AsignacionDTO> asignaciones = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return asignaciones;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    AsignacionDTO asignacion = new AsignacionDTO(partes[0], partes[1]);
                    asignaciones.add(asignacion);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de asignaciones. ", e);
        }
        return asignaciones;
    }

    public void guardarTodas(List<AsignacionDTO> asignaciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (AsignacionDTO asignacion : asignaciones) {
                bw.write(asignacion.getIdBibliotecario() + "," + 
                         asignacion.getIdLector());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de asignaciones ", e);
        }
    }

    @Override
    public boolean agregar(AsignacionDTO asignacion) {
        try {
            List<AsignacionDTO> lista = obtenerRegistros();
            
            // Verificamos si este lector ya tiene una asignación
            boolean existe = lista.stream().anyMatch(a -> a.getIdLector().equalsIgnoreCase(asignacion.getIdLector()));
            if (existe) {
                return false;
            }
            
            lista.add(asignacion);
            guardarTodas(lista);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar agregar una asignación: " + e.getMessage(), e);
        }
    }

    public AsignacionDTO obtenerPorLector(String idLector) {
        try {
            return obtenerRegistros().stream()
                    .filter(a -> a.getIdLector().equalsIgnoreCase(idLector))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Excepción al buscar la asignación por ID de lector: " + e.getMessage(), e);
        }
    }

    @Override
    public AsignacionDTO obtenerPorId(String idLector) {
        // Reutiliza la lógica del método anterior, ya que el ID que maneja este CRUD es el del Lector
        return obtenerPorLector(idLector);
    }

    @Override
    public boolean modificar(AsignacionDTO asignacionModificada) {
        try {
            List<AsignacionDTO> lista = obtenerRegistros();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getIdLector().equalsIgnoreCase(asignacionModificada.getIdLector())) {
                    lista.set(i, asignacionModificada);
                    guardarTodas(lista);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar modificar una asignación: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String idLector) {
        try {
            List<AsignacionDTO> lista = obtenerRegistros();
            boolean eliminada = lista.removeIf(a -> a.getIdLector().equalsIgnoreCase(idLector));

            if (eliminada) {
                guardarTodas(lista);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar eliminar una asignación: " + e.getMessage(), e);
        }
    }
}