package br.com.cinematoorafael.cinemamaisvoce.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EpisodioDTO (
        UUID id,
        Integer temporada,
        String titulo,
        Integer numeroEpsodio,
        Double avalicao,
        LocalDate dataLancamento){
}
