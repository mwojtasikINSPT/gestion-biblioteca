package dtos;

public class LectorDTO {
    private final String id;
    private final String dni;
    private final String nombre;
    private final String apellido;

    public LectorDTO(String id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    
    public String getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}