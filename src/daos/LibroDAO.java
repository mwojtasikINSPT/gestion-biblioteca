package daos;

import dtos.LibroDTO;
import models.Estado;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO implements ICrud<LibroDTO, String> {

    private final String ARCHIVO = "libros.txt";
    private final String ARCHIVO_HISTORICOS = "librosIdHistoricos.txt";

    @Override
    public List<LibroDTO> obtenerRegistros() {
        List<LibroDTO> libros = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return libros;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    LibroDTO libro = new LibroDTO(partes[0], partes[1], partes[2], Estado.valueOf(partes[3]));
                    libros.add(libro);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de libros.", e);
        }
        return libros;
    }

    public void guardarTodos(List<LibroDTO> libros) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (LibroDTO libro : libros) {
                bw.write(libro.getId() + ","
                        + libro.getTitulo() + ","
                        + libro.getAutor() + ","
                        + libro.getEstado());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de libros.", e);
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
            throw new RuntimeException("Error al leer el archivo de IDs históricos.", e);
        }
        return ids;
    }

    private void guardarIdHistorico(String id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_HISTORICOS, true))) {
            bw.write(id);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el ID histórico.", e);
        }
    }

    @Override
    public boolean agregar(LibroDTO libro) {
        try {
            List<LibroDTO> lista = obtenerRegistros();
            
            // Validamos si el libro ya existe para retornar false
            boolean existe = lista.stream().anyMatch(l -> l.getId().equalsIgnoreCase(libro.getId()));
            if (existe) {
                return false; 
            }
            
            lista.add(libro);
            guardarTodos(lista);
            guardarIdHistorico(libro.getId());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar agregar un libro: " + e.getMessage(), e);
        }
    }

    @Override
    public LibroDTO obtenerPorId(String id) {
        try {
            return obtenerRegistros().stream()
                    .filter(l -> l.getId().equalsIgnoreCase(id))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Excepción al buscar el libro por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean modificar(LibroDTO libroModificado) {
        try {
            List<LibroDTO> lista = obtenerRegistros();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equalsIgnoreCase(libroModificado.getId())) {
                    lista.set(i, libroModificado);
                    guardarTodos(lista);
                    return true;
                }
            }
            // Retorna false si recorrió toda la lista y no encontró el ID
            return false; 
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar modificar un libro: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String codigo) {
        try {
            List<LibroDTO> lista = obtenerRegistros();
            boolean eliminado = lista.removeIf(c -> c.getId().equalsIgnoreCase(codigo));

            if (eliminado) {
                guardarTodos(lista);
                return true;
            }
            
            // Retorna false si no se encontró el elemento a eliminar
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar eliminar un libro: " + e.getMessage(), e);
        }
    }
        
}