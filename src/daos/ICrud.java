package daos;
import java.util.List;

/**
 * Interfaz genérica para estandarizar los DAOs.
 * @param <T> La 'T' representa la clase de la entidad (ej: Asignacion, Libro).
 * @param <ID> El 'ID' representa el tipo de dato del identificador (ej: String, Integer).
 */

public interface ICrud<T, ID> {
    
    boolean agregar(T entidad);
    
    List<T> obtenerRegistros();
    
    T obtenerPorId(ID id);
    
    boolean modificar(T entidad);
    
    boolean eliminar(ID id);

    //default para poder agregar cuerpo y usar directo en cada DAO
    default boolean hayRegistros() {
        List<T> registros = obtenerRegistros();
        return registros != null && !registros.isEmpty();
    }
}