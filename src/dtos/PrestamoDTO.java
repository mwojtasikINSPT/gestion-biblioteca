package dtos;

//Viaja entre vista y controller
public class PrestamoDTO {
    private final String idPrestamo;
    private final String idLibro;
    private final String idLector;

    public PrestamoDTO(String idPrestamo, String idLibro, String idLector) {
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
        this.idLector = idLector;
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }
       
    public String getIdLibro() {
        return idLibro;
    }

    public String getIdLector() {
        return idLector;
    }

    

}