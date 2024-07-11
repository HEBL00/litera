package com.alurachallegenge.lite.repository;

import com.alurachallegenge.lite.model.Book;
import com.alurachallegenge.lite.model.LanguagesOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioLibros extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainsIgnoreCase(String bookName);


    List<Book> findByLanguage(LanguagesOptions language);

    Book findByTitle(String title);

}