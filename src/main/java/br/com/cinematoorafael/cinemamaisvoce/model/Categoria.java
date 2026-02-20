package br.com.cinematoorafael.cinemamaisvoce.model;

public enum Categoria {

    ACAO("Action"),
    AVENTURA("Adventure"),
    ANIMACAO("Animation"),
    BIOGRAFIA("Biography"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    DOCUMENTARIO("Documentary"),
    DRAMA("Drama"),
    FANTASIA("Fantasy"),
    FICCAO_CIENTIFICA("Sci-Fi"),
    GUERRA("War"),
    HISTORIA("History"),
    MISTERIO("Mystery"),
    MUSICAL("Musical"),
    ROMANCE("Romance"),
    SUSPENSE("Thriller"),
    TERROR("Horror"),
    ESPORTE("Sport"),
    FAMILIA("Family"),
    FAROESTE("Western"),
    POLICIAL("Crime"),
    SUPER_HEROI("Superhero"),
    THRILLER("Thriller"),
    OUTRO("Unknown");


    private String categoriaOmdb;

    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
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
}
