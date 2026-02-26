package br.com.cinematoorafael.cinemamaisvoce.service;

import br.com.cinematoorafael.cinemamaisvoce.dto.SerieDTO;
import br.com.cinematoorafael.cinemamaisvoce.model.Serie;
import br.com.cinematoorafael.cinemamaisvoce.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class SerieService {


    @Autowired
    private SeriesRepository repositorio;

    public List<SerieDTO> obterTodasSeries() {
        return converteDados(repositorio.findAll());

    }

    public Page<SerieDTO> obterTop5Series() {
        Pageable pageable = PageRequest.of(0, 5);
        return convertDados(repositorio.findAllByOrderByAvaliacaoDesc(pageable));

    }

    public List<SerieDTO> obterLancamento() {
        Pageable pageable = PageRequest.of(0, 5);
        return converteDados(repositorio.findTop5ByOrderByEpisodiosDataLancamentoDesc(pageable).getContent());
    }



    //Aqui eu converto os dados para o formato DTO, para a lista

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero().name(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()
                )).collect(Collectors.toList());
    }

    //Aqui eu converto os dados para o formato DTO, para paginação

    private Page<SerieDTO>convertDados(Page<Serie> series) {
        return series
                .map(s -> new SerieDTO(
                s.getId(),
                s.getTitulo(),
                s.getTotalTemporadas(),
                s.getAvaliacao(),
                s.getGenero().name(),
                s.getAtores(),
                s.getPoster(),
                s.getSinopse()
        ));
    }

    public SerieDTO obterPorId(UUID id) {
        Optional<Serie> serie = repositorio.findById(id);

        if(serie.isPresent()){

            Serie s = serie.get();

            return new SerieDTO(
                    s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero().name(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse()
            );
        } else {
            throw new RuntimeException("Série não encontrada com o ID: " + id);
        }
    }
}
