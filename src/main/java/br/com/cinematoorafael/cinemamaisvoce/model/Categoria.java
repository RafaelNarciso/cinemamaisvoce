package br.com.cinematoorafael.cinemamaisvoce.model;

public enum Categoria {

    ACAO("Action", "Ação"),
    AVENTURA("Adventure", "Aventura"),
    ANIMACAO("Animation", "Animação"),
    BIOGRAFIA("Biography", "Biografia"),
    COMEDIA("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    DOCUMENTARIO("Documentary", "Documentário"),
    DRAMA("Drama", "Drama"),
    FANTASIA("Fantasy", "Fantasia"),
    FICCAO_CIENTIFICA("Sci-Fi", "Ficção Científica"),
    GUERRA("War", "Guerra"),
    HISTORIA("History", "História"),
    MISTERIO("Mystery", "Mistério"),
    MUSICAL("Musical", "Musical"),
    ROMANCE("Romance", "Romance"),
    SUSPENSE("Thriller", "Suspense"),
    TERROR("Horror", "Terror"),
    ESPORTE("Sport", "Esporte"),
    FAMILIA("Family", "Família"),
    FAROESTE("Western", "Faroeste"),
    SUPER_HEROI("Superhero", "Super-herói"),
    OUTRO("Unknown", "Outro");

    private String categoriaPortugues;

    private String categoriaOmdb;


    Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public String getCategoriaOmdb() {
        return categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        if (text == null || text.isBlank()) {
            return OUTRO;
        }

        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);

    }

    public static Categoria fromPortugues(String text) {
        if (text == null || text.isBlank()) {
            return OUTRO;
        }

        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
