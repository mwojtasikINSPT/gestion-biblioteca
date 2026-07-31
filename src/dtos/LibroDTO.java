package dtos;

import models.Estado;

public class LibroDTO {
    private final String id;
    private final String titulo;
    private final String autor;
    private Estado estado;

    public LibroDTO(String id, String titulo, String autor, Estado estado) {
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

       
}