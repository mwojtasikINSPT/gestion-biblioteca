package models;

public class Asignacion {
    private final String idAsignacion;
    private final String idBibliotecario;
    private final String idLector;

    public Asignacion(String idAsignacion, String idBibliotecario, String idLector) {
        // Inicializo los atributos de la entidad
        this.idAsignacion = idAsignacion;
        this.idBibliotecario = idBibliotecario;
        this.idLector = idLector;
    }

    public String getIdAsignacion() {
        return idAsignacion;
    }

    public String getIdBibliotecario() {
        return idBibliotecario;
    }

    public String getIdLector() {
        return idLector;
    }
}