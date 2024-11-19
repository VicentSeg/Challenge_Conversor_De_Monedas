package com.AluraCursos.ChallengeLiterAlura.Dto;

import com.AluraCursos.ChallengeLiterAlura.Model.DatosAutor;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record LibroDto(
        Long id,
        String titulo,
        List<AutoresDto> autor,
        String idiomas,
        Double numeroDeDescargas
) {
}
