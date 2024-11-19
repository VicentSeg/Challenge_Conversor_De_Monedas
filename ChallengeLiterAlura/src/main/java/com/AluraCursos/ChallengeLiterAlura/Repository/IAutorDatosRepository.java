package com.AluraCursos.ChallengeLiterAlura.Repository;

import com.AluraCursos.ChallengeLiterAlura.Model.AutorDatos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IAutorDatosRepository extends JpaRepository<AutorDatos, Long> {

    List<AutorDatos> findByNombreAutor(String nombreAutor);
    @Query("SELECT a FROM AutorDatos a LEFT JOIN FETCH a.librosDeAutores")
    List<AutorDatos> findAllWithLibros();
    @Query("SELECT a FROM AutorDatos a LEFT JOIN FETCH a.librosDeAutores WHERE LOWER(a.nombreAutor) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Optional<AutorDatos> findByNombreWithLibros(@Param("nombre") String nombre);
    @Query("SELECT a FROM AutorDatos a WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento >= :anio)")
    List<AutorDatos> findAutoresVivosEnAnio(@Param("anio") Integer anio);


}

