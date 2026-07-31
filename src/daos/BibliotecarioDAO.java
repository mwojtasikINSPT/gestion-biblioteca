package daos;

import dtos.BibliotecarioDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecarioDAO implements ICrud<BibliotecarioDTO, String> {

    private final String ARCHIVO = "bibliotecarios.txt";    
    private final String ARCHIVO_HISTORICOS = "bibliotecariosIdHistoricos.txt";

    @Override
    public List<BibliotecarioDTO> obtenerRegistros() {
        List<BibliotecarioDTO> bibliotecarios = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return bibliotecarios;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    BibliotecarioDTO bibliotecario = new BibliotecarioDTO(partes[0], partes[1], partes[2], partes[3]);
                    bibliotecarios.add(bibliotecario);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de bibliotecarios. ", e);
        }
        return bibliotecarios;
    }

    public void guardarTodos(List<BibliotecarioDTO> bibliotecarios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (BibliotecarioDTO bibliotecario : bibliotecarios) {
                bw.write(bibliotecario.getId() + "," + 
                         bibliotecario.getDni() + "," + 
                         bibliotecario.getNombre() + "," + 
                         bibliotecario.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de bibliotecarios ", e);
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
    public boolean agregar(BibliotecarioDTO bibliotecario) {
        try {
            List<BibliotecarioDTO> lista = obtenerRegistros();
            
            // Verificamos si el bibliotecario ya existe
            boolean existe = lista.stream().anyMatch(b -> b.getId().equalsIgnoreCase(bibliotecario.getId()));
            if (existe) {
                return false;
            }
            
            lista.add(bibliotecario);
            guardarTodos(lista);

            // Cada vez que se crea un bibliotecario, registramos su ID en el histórico
            guardarIdHistorico(bibliotecario.getId());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar agregar un bibliotecario: " + e.getMessage(), e);
        }
    }

    @Override
    public BibliotecarioDTO obtenerPorId(String id) {
        try {
            return obtenerRegistros().stream()
                    .filter(b -> b.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Excepción al buscar el bibliotecario por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean modificar(BibliotecarioDTO bibliotecarioModificado) {
        try {
            List<BibliotecarioDTO> lista = obtenerRegistros();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equalsIgnoreCase(bibliotecarioModificado.getId())) {
                    lista.set(i, bibliotecarioModificado);
                    guardarTodos(lista);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar modificar un bibliotecario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String id) {
        try {
            List<BibliotecarioDTO> lista = obtenerRegistros();
            boolean eliminado = lista.removeIf(b -> b.getId().equalsIgnoreCase(id));

            if (eliminado) {
                guardarTodos(lista);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar eliminar un bibliotecario: " + e.getMessage(), e);
        }
    }
}