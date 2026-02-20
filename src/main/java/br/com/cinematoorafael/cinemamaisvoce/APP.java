package br.com.cinematoorafael.cinemamaisvoce;

import br.com.cinematoorafael.cinemamaisvoce.principal.Principal;
import br.com.cinematoorafael.cinemamaisvoce.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class APP implements CommandLineRunner {

    @Autowired
    private SeriesRepository repository;

    public static void main(String[] args) {SpringApplication.run(APP.class, args);}

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repository);

        principal.exibeMenu();
    }
}
