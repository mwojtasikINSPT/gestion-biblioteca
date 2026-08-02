package daos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Asignacion;

public class AsignacionDAO implements ICrud<Asignacion, String> {

    private final String ARCHIVO = "asignaciones.txt";
    private final String ARCHIVO_HISTORICOS = "asignacionesIdHistoricos.txt";

    @Override
    public List<Asignacion> obtenerRegistros() {
        List<Asignacion> asignaciones = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return asignaciones;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                // Valido que existan al menos 3 partes
                if (partes.length >= 3) {
                    // Instancio el modelo real con sus 3 parámetros
                    Asignacion asignacion = new Asignacion(partes[0], partes[1], partes[2]);
                    asignaciones.add(asignacion);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de asignaciones. ", e);
        }
        return asignaciones;
    }

    public void guardarTodas(List<Asignacion> asignaciones) {
        // Sobrescribo el archivo escribiendo cada entidad y sus respectivos atributos
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Asignacion asignacion : asignaciones) {
                bw.write(asignacion.getIdBibliotecario() + ","
                        + asignacion.getIdLector());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de asignaciones ", e);
        }
    }

    public List<String> obtenerIdsHistoricos() {
        // Leo el archivo de históricos y retorno la lista de IDs registrados
        List<String> ids = new ArrayList<>();
        File file = new File(ARCHIVO_HISTORICOS);
        if (!file.exists()) {
            return ids;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    ids.add(linea.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de IDs históricos de asignaciones. ", e);
        }
        return ids;
    }

    private void guardarIdHistorico(String id) {
        // Agrego el nuevo ID al archivo histórico de forma incremental
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_HISTORICOS, true))) {
            bw.write(id);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el ID histórico de asignación. ", e);
        }
    }

    @Override
    public boolean agregar(Asignacion asignacion) {
        // Obtengo los registros actuales de la base de datos
        List<Asignacion> lista = obtenerRegistros();

        // Verifico si ya existe una asignación para este lector
        boolean existe = lista.stream().anyMatch(a -> a.getIdLector().equalsIgnoreCase(asignacion.getIdLector()));
        if (existe) {
            return false;
        }

        // Agrego la asignación, guardo todos los registros y registro el ID histórico
        lista.add(asignacion);
        guardarTodas(lista);
        guardarIdHistorico(asignacion.getIdAsignacion());
        return true;
    }

    public Asignacion obtenerPorLector(String idLector) {
        // Filtro los registros buscando coincidencia por el ID del lector
        return obtenerRegistros().stream()
                .filter(a -> a.getIdLector().equalsIgnoreCase(idLector))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Asignacion obtenerPorId(String id) {
        // Filtro los registros buscando coincidencia por el ID de la asignación
        return obtenerRegistros().stream()
                .filter(a -> a.getIdAsignacion().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean modificar(Asignacion asignacionModificada) {
        // Obtengo la lista para buscar y reemplazar el registro coincidente
        List<Asignacion> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdAsignacion().equalsIgnoreCase(asignacionModificada.getIdAsignacion())) {
                lista.set(i, asignacionModificada);
                guardarTodas(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        // Obtengo la lista y elimino el registro si coincide el ID de la asignación
        List<Asignacion> lista = obtenerRegistros();
        boolean eliminada = lista.removeIf(a -> a.getIdAsignacion().equalsIgnoreCase(id));

        if (eliminada) {
            // Guardo la lista actualizada conservando el ID en el registro histórico
            guardarTodas(lista);
            return true;
        }
        return false;
    }

}
