package daos;

import dtos.PrestamoDTO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO implements ICrud<PrestamoDTO, String> {

    private final String ARCHIVO = "prestamos.txt";

    @Override
    public List<PrestamoDTO> obtenerRegistros() {
        List<PrestamoDTO> prestamos = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return prestamos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    PrestamoDTO prestamo = new PrestamoDTO(partes[0], partes[1]);
                    prestamos.add(prestamo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de prestamos. ", e);
        }
        return prestamos;
    }

    public void guardarTodas(List<PrestamoDTO> prestamos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (PrestamoDTO prestamo : prestamos) {
                bw.write(prestamo.getIdLibro() + "," + 
                         prestamo.getIdLector());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo de prestamos ", e);
        }
    }

    @Override
    public boolean agregar(PrestamoDTO prestamo) {
        try {
            List<PrestamoDTO> lista = obtenerRegistros();
            
            boolean existe = lista.stream().anyMatch(p -> p.getIdLibro().equalsIgnoreCase(prestamo.getIdLibro()));
            if (existe) {
                return false;
            }
            
            lista.add(prestamo);
            guardarTodas(lista);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar agregar un prestamo: " + e.getMessage(), e);
        }
    }

    @Override
    public PrestamoDTO obtenerPorId(String codigoLibro) {
        try {
            return obtenerRegistros().stream()
                    .filter(p -> p.getIdLibro().equalsIgnoreCase(codigoLibro))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Excepción al buscar el prestamo por código de libro: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean modificar(PrestamoDTO prestamoModificado) {
        try {
            List<PrestamoDTO> lista = obtenerRegistros();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getIdLibro().equalsIgnoreCase(prestamoModificado.getIdLibro())) {
                    lista.set(i, prestamoModificado);
                    guardarTodas(lista);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar modificar un prestamo: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String codigoLibro) {
        try {
            List<PrestamoDTO> lista = obtenerRegistros();
            boolean eliminada = lista.removeIf(p -> p.getIdLibro().equalsIgnoreCase(codigoLibro));

            if (eliminada) {
                guardarTodas(lista);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Excepción al intentar eliminar un prestamo: " + e.getMessage(), e);
        }
    }
}