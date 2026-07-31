package dtos;

public class AsignacionDTO {
    private String idBibliotecario;
    private String idLector;

    public AsignacionDTO(String idBibliotecario, String idLector) {
        this.idBibliotecario = idBibliotecario;
        this.idLector = idLector;
    }
        
    public String getIdBibliotecario() {
        return idBibliotecario;
    }

    public String getIdLector() {
        return idLector;
    }

    

}