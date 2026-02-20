//package br.com.cinematoorafael.cinemamaisvoce.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@Service
//public class OpenAIService {
//
//    private final WebClient webClient;
//
//    public OpenAIService(@Value("${openai.api.key}") String apiKey) {
//        this.webClient = WebClient.builder()
//                .baseUrl("https://api.openai.com/v1")
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//
//    public String gerarSinopse(String titulo) {
//
//        Map<String, Object> requestBody = Map.of(
//                "model", "gpt-4o-mini",
//                "messages", new Object[]{
//                        Map.of("role", "user",
//                                "content", "Gere uma sinopse curta para a série " + titulo)
//                }
//        );
//
//        return webClient.post()
//                .uri("/chat/completions")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
//}
