package br.com.cinematoorafael.cinemamaisvoce.principal;

import br.com.cinematoorafael.cinemamaisvoce.model.*;
import br.com.cinematoorafael.cinemamaisvoce.repository.SeriesRepository;
import br.com.cinematoorafael.cinemamaisvoce.service.ConsumoAPI;
import br.com.cinematoorafael.cinemamaisvoce.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final String  ENDERECO ="https://www.omdbapi.com/?t=";
    private final String  API_KEY ="&apikey=d0bc9998";
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSeries> dadosSeries = new ArrayList<>();
    private SeriesRepository repository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie>serieBusca;


    public Principal(SeriesRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu(){
        var opcao = -1;
        while(opcao != 0){

            System.out.println("""
                    1 - Buscar séries
                    2 - buscar episodios
                    3 - listar séries buscada
                    4 - Buscar série por título
                    5 - Buscar séries por atores
                    6 - Buscar top 5 séries
                    7 - Buscar Séries por categoria
                    8 - Filtrar séries Por temporada e avaliacao
                    9 - buscar episódios por trecho do nome da série
                    10 - Buscar  top 5 episódios por série
                    11 - bucar episódios a artir de uma data
                    19 - Buscar top seleção de séries
                    18 - Buscar top séries por gênero
                    0 - Sair
                    """);


            try {
                opcao = Integer.parseInt(leitura.nextLine());

            }catch (NumberFormatException ex){
                System.out.println("Opção inválida. Por favor, digite um número válido.");
                continue;
            }

            switch (opcao){
                case 1  -> {buscarSerieWeb();break;}
                case 2  -> {buscarEpisodioPorSerie();break;}
                case 3  -> {listarSeriesBuscadas();break;}
                case 4  -> {buscarSeriePorTitulo();break;}
                case 5  -> {buscarSeriesPorAtores();break;}
                case 6  -> {buscarTopCincoSeries();break;}
                case 7  -> {buscarPorCategoria();break;}
                case 8  -> {filtrarSeriesPorTemporadaEAvaliacao();break;}
                case 9  -> {buscarEpisodioPorTrecho();break;}
                case 10  -> {topEpisodiosPorSerie();break;}
                case 11  -> {buscarEpisodiosDepoisDeUmaData();break;}




                case 18  -> {buscarSeriesPorGenero();break;}
                case 19  -> {buscarTopSelecaoSeries();break;}
                case 0  -> {System.out.println("Saindo ....");break;}
                default -> System.out.println("Opção Inválida ");
            }



        }

    }



    private void buscarSeriePorTitulo() {
        listarSeriesBuscadas();
        System.out.println("Digite o nome da Série para busca : ");
        var nomeSerie = leitura.nextLine();

        serieBusca = repository.findByTituloContainsIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("""
                    Dados da Série Encontrada:
                    
                    """);
            System.out.println(serieBusca.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void buscarSerieWeb(){
        DadosSeries dados = getDadosSerie();

        Serie serie = new Serie(dados);
        repository.save(serie);
        //dadosSeries.add(dados);
        System.out.println(dados);
    }




//    private DadosSeries getDadosSerie() {
//        System.out.println("Digite o nome da Série para busca : ");
//        var nomeSerie = leitura.nextLine();
//
//        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+"));
//        DadosSeries dados = conversor.obterDados(json,DadosSeries.class);
//        return dados;
//
//    }

    private DadosSeries getDadosSerie() {
    System.out.println("Digite o nome da Série para busca");
    var nomeSerie = leitura.nextLine();

    if (repository.existsByTituloIgnoreCase(nomeSerie)) {
        System.out.println("Série já cadastrada.");
        return null;
    }


    var url = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
    var json = consumo.obterDados(url);

    System.out.println("URL: " + url);
    System.out.println("JSON: " + json);

    return conversor.obterDados(json, DadosSeries.class);
}
    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Digite o nome da Série para buscar os episódios : ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repository.findByTituloContainsIgnoreCase(nomeSerie);

//        Optional<Serie> serie = series.stream()
//                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
//                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") +
                        "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }


            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .filter(d -> d.episodios() != null)
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());



            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);

        }

        System.out.println("Série não encontrada.");
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);

    }
    private void buscarSeriesPorAtores() {
        listarSeriesBuscadas();
        System.out.println("Digite o nome do Ator para busca : ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Por qual avaliação mínima deseja filtrar as séries? (0.0 a 10.0)");
        var avaliacaoMinima = leitura.nextDouble();
        leitura.nextLine(); // Consumir a nova linha após ler o número
        List<Serie> seriesEncontradas = repository.findByAtoresContainsIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacaoMinima);

        System.out.println("Séries em que " + nomeAtor+ "trabalhou em :\n");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));

    }

    private void buscarTopCincoSeries() {

        Pageable pageable = PageRequest.of(0, 5);

        List<Serie> top5Series =
                repository.findAllByOrderByAvaliacaoDesc(pageable).getContent();

        System.out.println("Top 5 Séries por Avaliação:\n");

        top5Series.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao())
        );
    }

    private void buscarTopSelecaoSeries() {
        System.out.println("Digite o número de séries que deseja listar para saber a top séries : ");
        int quantidade = leitura.nextInt();
        leitura.nextLine();

        Pageable pageable = PageRequest.of(0, quantidade, Sort.by("avaliacao").descending());

        List<Serie> todasSeries =
                repository.findAll(pageable).getContent();

        System.out.println("Sua top Serries : \n");
        todasSeries.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        System.out.println("--------------------------------------------------\n");

    }

    private void buscarSeriesPorGenero() {
        System.out.println("Digite o gênero para busca : ");
        var generoDigitado = leitura.nextLine().toUpperCase();

        System.out.println("Digite o número de séries que deseja listar para saber a top séries por gênero : ");
        int quantidade = leitura.nextInt();

        try {
            Categoria categoria = Categoria.valueOf(generoDigitado);
            Pageable pageable = PageRequest.of(0,quantidade, Sort.by("avaliacao").descending());
            List<Serie> series = repository.findByGeneroOrderByAvaliacaoDesc(categoria, pageable).getContent();
            series.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        }catch (IllegalArgumentException e){
            System.out.println("Gênero inválido. Por favor, tente novamente."+ e.getLocalizedMessage());
            return;
        }
    }


    private void buscarPorCategoria() {
        System.out.println("Deseja buscar por qual categoria? ");
        var nomeGenero = leitura.nextLine().toUpperCase();

        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);

        System.out.println("Séries encontradas para a categoria " + nomeGenero + ":\n");
        seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));

    }

    //Código omitido

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite um trecho do nome da série para buscar os episódios: ");
        var trechoEpisodio = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
//        episodiosEncontrados.forEach(e ->
//                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
//                        e.getSerie().getTitulo(), e.getTemporada(),
//                        e.getNumeroEpsodio(), e.getTitulo()));
        episodiosEncontrados.forEach(System.out::println);
    }

    private void topEpisodiosPorSerie() {
       buscarSeriePorTitulo();
       if(serieBusca.isPresent()){
           Serie serie = serieBusca.get();
              List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);

           topEpisodios.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                       e.getNumeroEpsodio(), e.getTitulo(),e.getAvalicao()));

       }
    }


    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();

            System.out.println("Digite a data de referência para buscar episódios lançados depois dela (formato: YYYY-MM-DD): ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repository.epsodioPorSerieEAno(serie,anoLancamento);


            episodiosAno.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpsodio(), e.getTitulo(),e.getAvalicao()));

        }
    }

}
