package com.alurachallegenge.lite;

import com.alurachallegenge.lite.principal.Principal;

import com.alurachallegenge.lite.repository.AuthorRepository;
import com.alurachallegenge.lite.repository.RepositorioLibros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Autor ryus
@SpringBootApplication
public class ChallengeLiteraturaApplication implements CommandLineRunner {
	@Autowired
	private RepositorioLibros bookRepository;
	@Autowired
	private AuthorRepository authorRepository;


	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraturaApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(bookRepository, authorRepository);

		principal.libros();
	}
}