package br.com.cinematoorafael.cinemamaisvoce.principal;

import br.com.cinematoorafael.cinemamaisvoce.model.DadosEpisodio;
import br.com.cinematoorafael.cinemamaisvoce.model.DadosSeries;
import br.com.cinematoorafael.cinemamaisvoce.model.DadosTemporada;
import br.com.cinematoorafael.cinemamaisvoce.model.Episodio;
import br.com.cinematoorafael.cinemamaisvoce.service.ConsumoAPI;
import br.com.cinematoorafael.cinemamaisvoce.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class PrincipalOlden {
    private Scanner leitura = new Scanner(System.in);
    private final String  ENDERECO ="https://www.omdbapi.com/?t=";
    private final String  API_KEY ="&apikey=d0bc9998";
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();


    public void exibeMenu(){
        System.out.println("Digite o nome da série para buscar : ");

        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        DadosSeries dados = conversor.obterDados(json, DadosSeries.class);
        System.out.println(dados.toString());

        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i<=dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace("", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }
        temporadas.forEach(System.out::println);


        temporadas.forEach(t -> t.episodios().forEach(e -> e.titulo()));

        //top 5 epspdio
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        System.out.println("\n ......Top 5 epsodios ......");
        dadosEpisodios.stream()
                .filter(e -> !e.avalicao() .equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avalicao).reversed())
                .limit(5)
                .forEach(System.out::println);



        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);



//        //buscar sempre o primeiro resultado
//        System.out.println("Digite um trecho do titulo do episodio : ");
//        var trechoTitulo = leitura.nextLine();
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("Encontrei o Episodios !! ");
//            System.out.println("temporada : "+ episodioBuscado.get().getTemporada() );
//        }else{
//            System.out.println("Episodio não encontrado !!!  ");
//        }



//        // Pesquisa por ano (filtra episódios por ano fornecido pelo usuário)
//        System.out.println("\nA parti de que ano voçe deseja ver os episódios");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporadas : " + e.getTemporada()+
//                                "Episodios : "+ e.getTitulo()+
//                                "Data Lançamento : "+ e.getDataLancamento().format(formatter)
//                ));

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvalicao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvalicao)));

        System.out.println("avalição por temporada "+ avaliacaoPorTemporada);


        DoubleSummaryStatistics estatica = episodios.stream().filter(e -> e.getAvalicao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvalicao));

        System.out.println("Media : "+String.format(Locale.US, "%.2f",estatica.getAverage()));

        //uma forma de imprimir como valor formatado com duas casas decimais apos a virgula
//        Map<Integer, Double> avaliacaoPorTemporadas = episodios.stream()
//                .filter(e -> e.getAvalicao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvalicao)));
//
//        System.out.println("\nAvaliação por temporada:");
//        avaliacaoPorTemporada.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey())
//                .forEach(entry -> System.out.println(
//                        "Temporada " + entry.getKey() + " = " + String.format(Locale.US, "%.2f", entry.getValue())
//                ));



    }
}
