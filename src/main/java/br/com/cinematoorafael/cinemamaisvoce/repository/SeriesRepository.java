package br.com.cinematoorafael.cinemamaisvoce.repository;

import br.com.cinematoorafael.cinemamaisvoce.model.Categoria;
import br.com.cinematoorafael.cinemamaisvoce.model.Episodio;
import br.com.cinematoorafael.cinemamaisvoce.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface SeriesRepository extends JpaRepository<Serie, UUID> {
    boolean existsByTituloIgnoreCase(String titulo);

    Optional<Serie> findByTituloContainsIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainsIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor,Double avaliacaoMinima);

    //List<Serie> findByTop5OrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    //Trabalhando com JPQL
    @Query("select s from Serie s where s.totalTemporadas <= :totaltemporadas AND s.avaliacao >= :avaliacao order by s.avaliacao desc")
    List<Serie> seriesPorTemporadaEAvaliacao(int totaltemporadas, double avaliacao);

        //ILIKE %:trechoEpisodio% = SOMENTE TUDO QUE TERMINA COM O TRECHO, MAS SE O TRECHO ESTIVER
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE  e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE  s = :serie ORDER BY e.avalicao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s= :serie AND YEAR(e.dataLancamento) >= :anoLancamento order BY e.dataLancamento DESC")
    List<Episodio> epsodioPorSerieEAno(Serie serie, int anoLancamento);

    //List<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc();

    Page<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc(Pageable pageable);

    //Aqui eu paginando para realizar a buscar conforme eu precisar EX: top5 ou top10 ou top20 etc...
    Page<Serie> findAllByOrderByAvaliacaoDesc(Pageable pageable);


    Page<Serie> findAll(Pageable pageable);

    Page<Serie>findByGeneroOrderByAvaliacaoDesc(Categoria genero, Pageable pageable);
}
