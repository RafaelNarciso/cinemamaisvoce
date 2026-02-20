package br.com.cinematoorafael.cinemamaisvoce.service;

import tools.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json,classe);
        }catch (RuntimeException e){
            throw  new RuntimeException(e);
        }

    }



}
