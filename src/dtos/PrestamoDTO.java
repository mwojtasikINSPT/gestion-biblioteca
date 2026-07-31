package dtos;

public class PrestamoDTO {
    private final String idLibro;
    private final String idLector;

    public PrestamoDTO(String idLibro, String idLector) {
        this.idLibro = idLibro;
        this.idLector = idLector;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public String getIdLector() {
        return idLector;
    }

    

}