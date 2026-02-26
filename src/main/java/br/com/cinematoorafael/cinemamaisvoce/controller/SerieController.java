package br.com.cinematoorafael.cinemamaisvoce.controller;

import br.com.cinematoorafael.cinemamaisvoce.dto.SerieDTO;
import br.com.cinematoorafael.cinemamaisvoce.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO>  obterSeries() {
        return service.obterTodasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obeterTop5Series() {
        return service.obterTop5Series().getContent();
    }


    @GetMapping("/lancamentos")
    public List<SerieDTO> obterSeriesLancamento() {
        return service.obterLancamento();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable UUID id) {

        return service.obterPorId(id);
    }







}
