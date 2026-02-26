package br.com.cinematoorafael.cinemamaisvoce.dto;


import java.util.UUID;

public record SerieDTO(
        UUID id,
        String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        String genero,
        String atores,
        String poster,
        String sinopse) {

}
