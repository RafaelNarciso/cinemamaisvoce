package br.com.cinematoorafael.cinemamaisvoce.repository;

import br.com.cinematoorafael.cinemamaisvoce.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;


public interface SeriesRepository extends JpaRepository<Serie, UUID> {
    boolean existsByTituloIgnoreCase(String titulo);

    Optional<Serie> findByTituloContainsIgnoreCase(String nomeSerie);
}
