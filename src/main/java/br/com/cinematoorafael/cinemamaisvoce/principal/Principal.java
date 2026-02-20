package br.com.cinematoorafael.cinemamaisvoce.principal;

import br.com.cinematoorafael.cinemamaisvoce.model.DadosSeries;
import br.com.cinematoorafael.cinemamaisvoce.model.DadosTemporada;
import br.com.cinematoorafael.cinemamaisvoce.model.Episodio;
import br.com.cinematoorafael.cinemamaisvoce.model.Serie;
import br.com.cinematoorafael.cinemamaisvoce.repository.SeriesRepository;
import br.com.cinematoorafael.cinemamaisvoce.service.ConsumoAPI;
import br.com.cinematoorafael.cinemamaisvoce.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

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
                    0 - Sair
                    """);

            opcao = leitura.nextInt();
            leitura.nextLine();


            switch (opcao){
                case 1  -> {buscarSerieWeb();break;}
                case 2  -> {buscarEpisodioPorSerie();break;}
                case 3  -> {listarSeriesBuscadas();break;}
                case 4  -> {buscarSeriePorTitulo();break;}
                case 0  -> {System.out.println("Saindo ....");break;}
                default -> System.out.println("Opção Inválida ");
            }



        }

    }

    private void buscarSeriePorTitulo() {
        listarSeriesBuscadas();
        System.out.println("Digite o nome da Série para busca : ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serieEncontrada = repository.findByTituloContainsIgnoreCase(nomeSerie);

        if (serieEncontrada.isPresent()) {
            System.out.println("""
                    Dados da Série Encontrada:
                    
                    """);
            System.out.println(serieEncontrada.get());
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


}
