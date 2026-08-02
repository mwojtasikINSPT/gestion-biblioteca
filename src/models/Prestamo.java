package models;

public class Prestamo {
    
    private String id;
    private String idLibro;
    private String idLector;

    public Prestamo(String id, String idLibro, String idLector) {
        this.id = id;
        this.idLibro = idLibro;
        this.idLector = idLector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getIdLector() {
        return idLector;
    }

    public void setIdLector(String idLector) {
        this.idLector = idLector;
    }
}