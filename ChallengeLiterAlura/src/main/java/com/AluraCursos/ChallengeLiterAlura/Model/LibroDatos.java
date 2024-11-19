package com.AluraCursos.ChallengeLiterAlura.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")
public class LibroDatos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long ID;
    @Column(unique = true)
    private String tituloLibro;
    @Column(name = "idiomas")
    private String idiomas;
    private Double numeroDeDescargas;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<AutorDatos> autorDatos = new ArrayList<>();

    public LibroDatos() {}
    public LibroDatos(DatosLibros datosLibros){
        this.tituloLibro = datosLibros.titulo();
        this.idiomas = String.join(",", datosLibros.idiomas());
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();

    }

    public List<AutorDatos> getAutorDatos() {
        return autorDatos;
    }

    public void setAutorDatos(List<AutorDatos> autorDatos) {
        this.autorDatos = autorDatos;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public List<String> getIdiomas() {
        return Arrays.asList(idiomas.split(","));
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = String.join(",", idiomas);
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}
