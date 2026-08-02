package daos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Prestamo;

public class PrestamoDAO implements ICrud<Prestamo, String> {

    private final String ARCHIVO = "prestamos.txt";
    private final String ARCHIVO_HISTORICOS = "prestamosIdHistoricos.txt";

    @Override
    public List<Prestamo> obtenerRegistros() {
        List<Prestamo> prestamos = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return prestamos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3) {
                    Prestamo prestamo = new Prestamo(partes[0], partes[1], partes[2]);
                    prestamos.add(prestamo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de prestamos. ", e);
        }
        return prestamos;
    }

    public void guardarTodos(List<Prestamo> prestamos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Prestamo prestamo : prestamos) {
                bw.write(prestamo.getId() + "," + 
                         prestamo.getIdLibro() + "," + 
                         prestamo.getIdLector());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de prestamos ", e);
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
            throw new RuntimeException("Error al leer el archivo de IDs históricos de préstamos. ", e);
        }
        return ids;
    }

    private void guardarIdHistorico(String id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_HISTORICOS, true))) {
            bw.write(id);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el ID histórico de préstamo. ", e);
        }
    }

    @Override
    public boolean agregar(Prestamo prestamo) {
        // Obtengo los registros actuales
        List<Prestamo> lista = obtenerRegistros();
        
        // Verifico si el préstamo ya existe
        boolean existe = lista.stream().anyMatch(p -> p.getId().equalsIgnoreCase(prestamo.getId()));
        if (existe) {
            return false;
        }
        
        // Agrego a la lista y persisto los cambios
        lista.add(prestamo);
        guardarTodos(lista);
        
        // Registro el ID en el histórico
        guardarIdHistorico(prestamo.getId());
        return true;
    }

    @Override
    public Prestamo obtenerPorId(String id) {
        // Filtro y devuelvo el registro coincidente
        return obtenerRegistros().stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean modificar(Prestamo prestamoModificado) {
        // Obtengo la lista para buscar y reemplazar el registro
        List<Prestamo> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(prestamoModificado.getId())) {
                lista.set(i, prestamoModificado);
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String id) {
        // Obtengo la lista y elimino el registro si coincide el ID
        List<Prestamo> lista = obtenerRegistros();
        boolean eliminado = lista.removeIf(p -> p.getId().equalsIgnoreCase(id));

        if (eliminado) {
            // Guardo la lista actualizada conservando el ID en el registro histórico
            guardarTodos(lista);
            return true;
        }
        return false;
    }
    
    public Prestamo obtenerPrestamoPorLibro(String idLibro) {
        // Filtro los préstamos buscando por el ID del libro
        return obtenerRegistros().stream()
                .filter(p -> p.getIdLibro().equalsIgnoreCase(idLibro))
                .findFirst()
                .orElse(null);
    }
}