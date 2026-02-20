package br.com.cinematoorafael.cinemamaisvoce.model;

import br.com.cinematoorafael.cinemamaisvoce.service.ConsultaChatGPT;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "series")
public class Serie {
    //regions Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Episodio> episodios = new ArrayList<>();
    //endregion

    protected Serie() {
    }

    //region Construtores
    public Serie(DadosSeries dadosSeries) {
        this.titulo = dadosSeries.titulo();
        this.totalTemporadas = dadosSeries.totalTemporadas();

        //this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSeries.avaliacao())).orElse(0);
        String av = dadosSeries.avaliacao();
        if (av == null || av.isBlank() || av.equalsIgnoreCase("N/A")) {
            this.avaliacao = 0.0;
        } else {
            try {
                this.avaliacao = Double.parseDouble(av);
            } catch (NumberFormatException e) {
                this.avaliacao = 0.0;
            }
        }


       this.genero = Categoria.fromString(dadosSeries.genero().split(",")[0].trim());
//        String gen = dadosSeries.genero();
//        this.genero = (gen == null || gen.isBlank() || gen.equalsIgnoreCase("N/A"))
//                ? Categoria.OUTRO
//                : Categoria.fromString(gen.split(",")[0].trim());

        this.atores = dadosSeries.atores();
        this.poster = dadosSeries.poster();
        //this.sinopse = ConsultaChatGPT.obterTraducao(dadosSeries.sinopse()).trim();
    }

    //endregion

        //para manipular a chave estrangeira de episodio para serie isso no set
    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return """
                ===== SÉRIE =====
                Gênero: %s
                Título: %s
                Total de Temporadas: %d
                Avaliação: %s
                Atores: %s
                Poster: %s
                Sinopse: %s
                Epsodios: %s
                """.formatted(
                genero,
                titulo,
                totalTemporadas,
                avaliacao,
                atores,
                poster,
                sinopse,
                episodios
        );
    }
}
