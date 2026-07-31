package models;

public class Libro {
    private String id;
    private String titulo;
    private String autor;
    private Estado estado;

    public Libro(String id, String titulo, String autor, Estado estado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }


    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}