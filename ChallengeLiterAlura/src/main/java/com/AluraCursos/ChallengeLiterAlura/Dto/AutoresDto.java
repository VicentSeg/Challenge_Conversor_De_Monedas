package com.AluraCursos.ChallengeLiterAlura.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutoresDto (
        Long id,
        String nombreAutor,
        String fechaDeNacimiento,
        String fechaDeFallecimiento){
}