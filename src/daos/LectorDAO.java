package daos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Lector;

public class LectorDAO implements ICrud<Lector, String> {

    private final String ARCHIVO = "lectores.txt";
    private final String ARCHIVO_HISTORICOS = "lectoresIdHistoricos.txt";

    @Override
    public List<Lector> obtenerRegistros() {
        List<Lector> lectores = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return lectores;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    Lector lector = new Lector(partes[0], partes[1], partes[2], partes[3]);
                    lectores.add(lector);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de lectores. ", e);
        }
        return lectores;
    }

    public void guardarTodos(List<Lector> lectores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Lector lector : lectores) {
                bw.write(lector.getId() + "," + 
                         lector.getDni() + "," + 
                         lector.getNombre() + "," + 
                         lector.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de lectores ", e);
        }
    }

    public List<String> obtenerIdsHistoricos() {
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
            throw new RuntimeException("Error al leer el archivo de IDs históricos. ", e);
        }
        return ids;
    }

    private void guardarIdHistorico(String id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_HISTORICOS, true))) {
            bw.write(id);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el ID histórico. ", e);
        }
    }

    @Override
    public boolean agregar(Lector lector) {
        // Obtengo los registros actuales
        List<Lector> lista = obtenerRegistros();
        
        // Verifico si el lector ya existe
        boolean existe = lista.stream().anyMatch(l -> l.getId().equalsIgnoreCase(lector.getId()));
        if (existe) {
            return false;
        }
        
        // Agrego a la lista y persisto los cambios
        lista.add(lector);
        guardarTodos(lista);
        
        // Registro el ID en el histórico
        guardarIdHistorico(lector.getId());
        return true;
    }

    @Override
    public Lector obtenerPorId(String id) {
        // Filtro y devuelvo el registro coincidente
        return obtenerRegistros().stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean modificar(Lector lectorModificado) {
        // Obtengo la lista para buscar y reemplazar el registro
        List<Lector> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(lectorModificado.getId())) {
                lista.set(i, lectorModificado);
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        // Obtengo la lista y elimino el registro si coincide el ID
        List<Lector> lista = obtenerRegistros();
        boolean eliminado = lista.removeIf(l -> l.getId().equalsIgnoreCase(id));

        if (eliminado) {
            // Guardo la lista actualizada conservando el ID en el registro histórico
            guardarTodos(lista);
            return true;
        }
        return false;
    }
}