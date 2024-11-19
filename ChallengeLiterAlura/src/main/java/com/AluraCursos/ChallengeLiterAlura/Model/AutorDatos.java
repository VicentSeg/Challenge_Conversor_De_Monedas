package com.AluraCursos.ChallengeLiterAlura.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "autores")
public class AutorDatos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long ID;
    private String nombreAutor;
    private String fechaDeNacimiento;
    private String fechaDeFallecimiento;
    @ManyToMany(mappedBy = "autorDatos", fetch = FetchType.LAZY)
    private List<LibroDatos> librosDeAutores = new ArrayList<>();

    public AutorDatos (){}
    public AutorDatos (DatosAutor datosAutor){
        this.nombreAutor = datosAutor.nombreAutor();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }
    public List<LibroDatos> getLibrosDeAutores() {
        return librosDeAutores;
    }
    public void setLibrosDeAutores(List<LibroDatos> librosDeAutores) {
        this.librosDeAutores = librosDeAutores;
    }
    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;}

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
