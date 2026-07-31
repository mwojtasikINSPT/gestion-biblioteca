package utils;


import java.util.ArrayList;
import java.util.List;


public class Validaciones {

// Método para generar IDs alfanuméricos (ej: P0001, E0001). Static: pertenece a la clase    
    public static String generarSiguienteId(List<String> idsExistentes, String prefijo) {
        int maxId = 0;
        
        // Normalizamos el prefijo recibido para que coincida en mayúsculas
        String prefijoUpper = prefijo != null ? prefijo.toUpperCase() : "";

        for (String id : idsExistentes) {
            // Normalizamos también el ID existente por si había alguno guardado en minúscula
            String idUpper = id != null ? id.toUpperCase() : "";
            
            if (idUpper.startsWith(prefijoUpper)) {
                try {
                    String numeroStr = idUpper.substring(prefijoUpper.length());
                    int numero = Integer.parseInt(numeroStr);

                    if (numero > maxId) {
                        maxId = numero;
                    }
                } catch (NumberFormatException e) {
                    // Ignora formatos corruptos
                }
            }
        }
        // "%04d" asegura que siempre haya al menos 4 dígitos, rellenando con ceros.
        // Ej: maxId 5 -> "P0006". maxId 1500 -> "P1501".
        return prefijoUpper + String.format("%04d", maxId + 1);
    }

    //Sobrecargo el metodo para que no repita ids que hayan existido alguna vez
    public static String generarSiguienteId(List<String> idsActivos, List<String> idsHistoricos, String prefijo) {
        List<String> todosLosIds = new ArrayList<>(idsActivos);
        if (idsHistoricos != null) {
            todosLosIds.addAll(idsHistoricos);
        }
        return generarSiguienteId(todosLosIds, prefijo);
    }

    // Valida que el DNI tenga exactamente 8 numeros
    public static boolean esDniValido(String dni) {
        if (dni == null) {
            return false;
        }
        // \\d significa "digito numerico", y {8} significa "exactamente 8 veces"
        return dni.matches("\\d{8}");
    }

    // Valida que el texto no este vacio ni compuesto solo por espacios
    public static boolean esTextoValido(String texto) {
        if (texto == null) {
            return false;
        }
        // trim() quita los espacios al principio y al final
        return !texto.trim().isEmpty();
    }


    // Valida que el texto ingresado sea un numero entero mayor a 0
    public static boolean esNumeroPositivo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            int numero = Integer.parseInt(texto.trim());
            return numero > 0;
        } catch (NumberFormatException e) {
            return false; // Si tiene letras, cae aca y devuelve falso
        }
    }

    // Normaliza un texto para búsquedas (quita espacios extremos y lo pasa a mayúsculas)
    public static String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toUpperCase();
    }


}
