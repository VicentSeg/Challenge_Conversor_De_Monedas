package com.AluraCursos.ChallengeLiterAlura.Repository;
import com.AluraCursos.ChallengeLiterAlura.Model.LibroDatos;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ILibroDatosRepository extends JpaRepository<LibroDatos, Long>{
    @EntityGraph(attributePaths = "autorDatos")
    Optional<LibroDatos> findByTituloLibro(String tituloLibro);
    @Query("SELECT l FROM LibroDatos l LEFT JOIN FETCH l.autorDatos")
    List<LibroDatos> findAllWithAutores();
    // Método para buscar libros por título (parcial o completo)
    List<LibroDatos> findByTituloLibroContainingIgnoreCase(String tituloLibro);
    @Query("SELECT l FROM LibroDatos l WHERE l.idiomas = :idioma")
    List<LibroDatos> findByIdioma(@Param("idioma") String idioma);
}

