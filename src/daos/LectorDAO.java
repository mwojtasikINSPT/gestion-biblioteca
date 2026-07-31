package daos;

import dtos.LectorDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LectorDAO implements ICrud<LectorDTO, String> {

    private final String ARCHIVO = "lectores.txt";
    private final String ARCHIVO_HISTORICOS = "lectoresIdHistoricos.txt";

    @Override
    public List<LectorDTO> obtenerRegistros() {
        List<LectorDTO> lectores = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return lectores;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    LectorDTO lector = new LectorDTO(partes[0], partes[1], partes[2], partes[3]);
                    lectores.add(lector);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de lectores. ", e);
        }
        return lectores;
    }

    public void guardarTodos(List<LectorDTO> lectores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (LectorDTO lector : lectores) {
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

    // Método para leer todos los IDs que alguna vez existieron
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

    // Método para agregar un ID al archivo histórico (usando append = true)
    private void guardarIdHistorico(String id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_HISTORICOS, true))) {
            bw.write(id);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el ID histórico. ", e);
        }
    }

    @Override
    public boolean agregar(LectorDTO lector) {
        try {
            List<LectorDTO> lista = obtenerRegistros();
            
            // Verificamos si el lector ya existe
            boolean existe = lista.stream().anyMatch(l -> l.getId().equalsIgnoreCase(lector.getId()));
            if (existe) {
                return false;
            }
            
            lista.add(lector);
            guardarTodos(lista);
            
            // Cada vez que se crea un lector, registramos su ID en el histórico
            guardarIdHistorico(lector.getId());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar agregar un lector: " + e.getMessage(), e);
        }
    }

    @Override
    public LectorDTO obtenerPorId(String id) {
        try {
            return obtenerRegistros().stream()
                    .filter(l -> l.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Excepción al buscar el lector por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean modificar(LectorDTO lectorModificado) {
        try {
            List<LectorDTO> lista = obtenerRegistros();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equalsIgnoreCase(lectorModificado.getId())) {
                    lista.set(i, lectorModificado);
                    guardarTodos(lista);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar modificar un lector: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String id) {
        try {
            List<LectorDTO> lista = obtenerRegistros();
            boolean eliminado = lista.removeIf(l -> l.getId().equalsIgnoreCase(id));

            if (eliminado) {
                guardarTodos(lista);
                // Al eliminar de "lectores.txt", el ID NO se borra de "lectoresIdHistoricos.txt", 
                // logrando así que el generador sepa que ese ID ya fue usado.
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar eliminar un lector: " + e.getMessage(), e);
        }
    }
}