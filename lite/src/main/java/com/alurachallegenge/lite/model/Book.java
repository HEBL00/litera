package com.alurachallegenge.lite.model;

import jakarta.persistence.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String title;
    @ManyToOne
//    @JoinColumn(name = "author_id")
    private Author author;
    @Enumerated(EnumType.STRING)
    private LanguagesOptions language;
    private int downloadCount;

    public Book() {
    }


    public Book(BookData book, Author author) {
        this.title = book.title();

        this.language = book.languages().stream()
                .map(LanguagesOptions::getNameByCode)
                .collect(Collectors.toList())
                .get(0);
        this.downloadCount = book.downloadCount();
        this.author = author;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LanguagesOptions getLanguage() {
        return language;
    }

    public void setLanguage(LanguagesOptions language) {
        this.language = language;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {

        return

                "\n   Título: " + title +
                        "\n   Autor: " + author +
                        "\n   Idioma: " + language +
                        "\n   Número de descargas: " + downloadCount;

    }


}
