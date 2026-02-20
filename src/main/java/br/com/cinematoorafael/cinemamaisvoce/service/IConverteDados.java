package br.com.cinematoorafael.cinemamaisvoce.service;

public interface IConverteDados {

    <T> T obterDados(String json,Class<T> classe);

}
