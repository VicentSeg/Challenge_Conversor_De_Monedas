package com.aluracursos.screenmatch_frases.Service;

import com.aluracursos.screenmatch_frases.DTO.FrasesDTO;
import com.aluracursos.screenmatch_frases.Model.Frase;
import com.aluracursos.screenmatch_frases.Repository.FraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrasesService {
    @Autowired
    private FraseRepository repository;
    public FrasesDTO obtenerFraseAleatoria() {
        Frase frase = repository.obtenerFraseAleatoria();
        return  new FrasesDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonaje(),frase.getPoster());
    }
}
