package utils;

public class Mensajes {

    
    // Usamos 'final' para que nadie pueda cambiar el texto durante la ejecucion
    // Menus
    public static final String OPCION_INVALIDA = "Opcion invalida. Intente de nuevo.";
    public static final String VOLVIENDO = "\nVolviendo al Menu Anterior...";
    public static final String SALIENDO = "\nSaliendo del Sistema. Hasta Luego!";

    // CRUD
    public static final String PEDIR_NUEVOS_DATOS = "Ingrese los NUEVOS datos ";

    public static final String EXITO_GUARDAR = "Registro guardado con exito.";
    public static final String EXITO_ACTUALIZAR = "Registro actualizado con exito.";
    public static final String EXITO_ELIMINAR = "Registro eliminado con exito.";
    public static final String EXITO_REASIGNAR = "Registro reasignado";

    public static final String ERROR_GUARDAR = "Error: No se pudo guardar al archivo de datos";
    public static final String ERROR_ELIMINAR = "Error: No se pudo eliminar el registro";
    public static final String ERROR_LECTURA = "Error: No se pudo acceder al archivo de datos";
    public static final String ERROR_NO_ENCONTRADO = "Error: No se encontro el registro con el identificador ingresado.";
    public static final String ERROR_ID = "Error: No se encontro el ID ingresado.";
    public static final String ERROR_ACTUALIZAR = "Error: No se pudo actualizar el registro.";
    public static final String ERROR_ELIMINAR_EN_USO = "Error: No se pudo eliminar, el registro esta en uso o tiene entidades relacionadas";
    public static final String ERROR_SIN_CAMBIOS = "Los datos coinciden con los anteriores.";
    
    public static final String ERROR_DATO = "Error: Ingrese un dato valido";
    public static final String PEDIR_DATO = "Ingrese ";
    public static final String DATO_DUPLICADO = "Error: Ya existe ese dato registrado";
    
    public static final String SIN_REGISTROS = "No hay registros";
}
