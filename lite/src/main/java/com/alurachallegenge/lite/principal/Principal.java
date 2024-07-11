package com.alurachallegenge.lite.principal;

import com.alurachallegenge.lite.exceptions.BookNotFoundException;
import com.alurachallegenge.lite.exceptions.InvalidOptionsException;
import com.alurachallegenge.lite.model.*;
import com.alurachallegenge.lite.repository.AuthorRepository;
import com.alurachallegenge.lite.repository.RepositorioLibros;
import com.alurachallegenge.lite.service.ConsumeAPI;
import com.alurachallegenge.lite.service.ConvertData;
import com.alurachallegenge.lite.validations.AllValidations;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner teclado = new Scanner(System.in);
    private ConsumeAPI consumeAPI = new ConsumeAPI();
    private final String URL_BASE = "https://gutendex.com/books/";

    private ConvertData converter = new ConvertData();
    private RepositorioLibros bookRepository;
    private AuthorRepository authorRepository;
    private Boolean isRunningApp = true;
    private AllValidations validations = new AllValidations();


    public Principal(RepositorioLibros bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }


    public void libros() {

        while (isRunningApp) {

            try {
                showMenu();

                System.out.print("Ingresa el número de la opción que desea ejecutar:  ");
                int selectedOption = teclado.nextInt();
                teclado.nextLine();
                validations.verifyMenuInputIsValid(selectedOption);


                switch (selectedOption) {
                    case 1:
                        searchBooksbyTitle();
                        break;
                    case 2:
                        listRegisteredBooks();
                        break;
                    case 3:
                        listRegisteredAuthors();
                        break;
                    case 4:
                        listAuthorsByRangeYear();
                        break;
                    case 5:
                        listBooksByLanguages();
                        break;
                    case 6:
                        getBookAndAuthorDataFromGutendex();
                        break;
                    case 0:
                        isRunningApp = false;
                        System.out.println(" ");
                        System.exit(0);

                    default:
                        System.out.println("Opción inválida, intente de nuevo");
                }

            } catch (InputMismatchException e) {
                teclado.nextLine();
                System.out.println("Error: Entrada inválida. Intente de nuevo.");
            } catch (InvalidOptionsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void showMenu() {
        var menu = """
                   ___________ MENU DE OPCIONES __________________
                    
                    1. Buscar libros por título 
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idiomas
                    6. Buscar libros por título en el servidor
                    
                    0. Salir
                    ______________________________________________
                """;
        System.out.println(menu);

    }

    private void searchBooksbyTitle() throws InvalidOptionsException {
        System.out.print("Ingrese el título del libro que desea buscar:  ");
        var searchedBookByTitle = teclado.next();
        teclado.nextLine();
        List<Book> booksList = bookRepository.findByTitleContainsIgnoreCase(searchedBookByTitle);

        int count = 1;
        if (!booksList.isEmpty()) {
            for (Book book : booksList) {

                System.out.println("\n------------------- LIBRO " + count + " -------------------");
                showBook(book, book.getAuthor());
                System.out.println("\n------------------- ***** -------------------\n");
                count++;

            }

            Book searchedBook = null;

            if (booksList.stream().count() > 1) {
                System.out.print("Inserte el número que se encuentra en el encabezado para ver el libro que desea, si desea salir insertar 0: ");
                var selectedBook = teclado.nextInt();
                teclado.nextLine();

                validations.verifyGutendexInputIsValid(selectedBook, booksList.size());

                searchedBook = booksList.get(selectedBook - 1);

            } else if (booksList.stream().count() == 1) {
                searchedBook = booksList.get(0);
            } else {
                System.out.println("No se encontró ese libro en Gutendex :(");

            }

            System.out.println("\n------------------- LIBRO  ------------------- ");
            showBook(searchedBook, searchedBook.getAuthor());
            System.out.println("\n------------------- ***** ------------------- \n");

        } else {
            System.out.println("Libro no encontrado en la base de datos");
        }
    }

    private void listRegisteredBooks() {


        List<Book> allRegisteredBooks = bookRepository.findAll();

        if (!allRegisteredBooks.isEmpty()) {
            int count = 1;
            System.out.println("\nEstos son todos los libros registrados en la base de datos");
            for (Book book : allRegisteredBooks) {

                System.out.println("\n------------------- LIBRO " + count + " -------------------");
                showBook(book, book.getAuthor());
                System.out.println("\n------------------- ***** -------------------\n");
                count++;

            }
        } else {
            System.out.println("No hay libros almacenados en la base de datos");
        }
    }

    private void listRegisteredAuthors() {
        List<Author> allAuthors = authorRepository.findAll();
        System.out.println("Esos son todos los autores registrados en la base de datos");
        showAuthors(allAuthors);


    }

    private void listAuthorsByRangeYear() throws InvalidOptionsException {
        System.out.println("*Para años antes de Cristo deben ser negativos (ej. -499 para 499 a.C.)");
        System.out.print("Ingresa el año que deseas: ");
        var inputYear = teclado.nextInt();


        validations.verifyYearsFormat(inputYear);
        List<Author> searchedAuthors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(inputYear, inputYear);
        List<String> authorBooks = new ArrayList<>();
        int count = 1;

        showAuthors(searchedAuthors);

    }

    private void listBooksByLanguages() throws InvalidOptionsException {
        System.out.println("""
                Menú de opciones:
                1. Inglés
                2. Alemán
                3. Español
                4. Italiano
                5. Rusia
                6. Chino
                7. Portugués
                """);
        System.out.print("Selecciona el número del idioma de los libros que deseas obtener: ");
        var inputLanguage = teclado.nextInt();
        validations.verifyOptionsForLanguageMeu(inputLanguage);

        LanguagesOptions selectedLanguage = null;

        switch (inputLanguage) {
            case 1:
                selectedLanguage = LanguagesOptions.ENGLISH;
                break;
            case 2:
                selectedLanguage = LanguagesOptions.GERMAN;
                break;
            case 3:
                selectedLanguage = LanguagesOptions.SPANISH;
                break;

            default:
                System.out.println("Error, no se encontrarón libros con ese idioma ");
        }


        List<Book> booksByLanguage = bookRepository.findByLanguage(selectedLanguage);

        if (!booksByLanguage.isEmpty()) {
            int count = 1;
            for (Book book : booksByLanguage) {

                System.out.println("\n------------------- LIBRO " + count + " -------------------");
                showBook(book, book.getAuthor());
                System.out.println("\n------------------- ***** -------------------\n");
                count++;

            }
        } else {
            System.out.println("No hay libros registrados en ese idioma. ");
        }
    }

    private void getBookAndAuthorDataFromGutendex() throws InvalidOptionsException {

        ResultsData data = getBookFromAPI();

        //Verify if the ResultData variable is empty
        if (!data.results().isEmpty()) {

            List<BookData> foundBooksList = data.results();
            Set<String> uniqueTitles = new HashSet<>();
            List<BookData> uniqueBooks = new ArrayList<>();


            for (BookData book : foundBooksList) {
                String title = book.title();
                if (!uniqueTitles.contains(title)) {
                    uniqueTitles.add(title);
                    uniqueBooks.add(book);
                }
            }


            System.out.println("\n- | - | - | - | - | - | - ENTRADA A LA BIBLIOTECA - | - | - | - | - | - | -\n");
            int count = 1;


            String languageInSpanish;
            for (BookData book : uniqueBooks) {
                var languageInEnglish = book.languages().stream().map(LanguagesOptions::getNameByCode)
                        .collect(Collectors.toList()).get(0);
                languageInSpanish = LanguagesOptions.getSpanishNameByCode(String.valueOf(languageInEnglish));


                System.out.println(
                        "------------------- LIBRO " + count + " -------------------" +


                                "\n   Título: " + book.title() +
                                "\n   Autor: " + book.authors().get(0).name() +
                                "\n   Idioma: " + languageInSpanish +
                                "\n   Número de descargas: " + book.downloadCount() +

                                "\n------------------- ***** ------------------- \n"
                );
                count++;

            }
            System.out.println("\n- | - | - | - | - | - | - SALIDA DE LA BIBLIOTECA - | - | - | - | - | - | -\n");



            BookData searchedBook = null;

            if (uniqueBooks.stream().count() > 1) {
                System.out.print("Inserte el número que se encuentra en el encabezado para almacenar el libro que desea, si desea salir insertar 0: ");
                var selectedBook = teclado.nextInt();
                teclado.nextLine();

                validations.verifyGutendexInputIsValid(selectedBook, uniqueBooks.size());

                searchedBook = uniqueBooks.get(selectedBook - 1);

            } else if (uniqueBooks.stream().count() == 1) {
                searchedBook = uniqueBooks.get(0);
            } else {
                System.out.println("No se encontró ese libro en Gutendex :(");
            }


            AuthorData authorData = searchedBook.authors().get(0);
            Book isBookInDB = bookRepository.findByTitle(searchedBook.title());
            Author isAuthorInDB = authorRepository.findByName(authorData.name());


            if (isBookInDB == null) {
                Author author;
                if (isAuthorInDB == null) {

                    author = new Author(authorData);
                    authorRepository.save(author);
                } else {
                    author = isAuthorInDB;
                }

                saveBookData(searchedBook, author);
                showBookData(searchedBook, author);

            } else {
                System.out.println("\n--- El libro ya se encuentra en la base de datos.  ---\n");
            }
        } else {
            System.out.println("\nError, no se encontró información sobre el libro o su autor.");
        }
    }

    private ResultsData getBookFromAPI() {

        ResultsData data = null;

        try {
            System.out.print("Ingrese el título del libro que desea : ");

            var bookTitle = teclado.nextLine();
            var json = consumeAPI.getData(URL_BASE + "/?search=" + bookTitle.replace(" ", "%20"));
            System.out.println(json);
            data = converter.getData(json, ResultsData.class);
            validations.verifyIsnotNullData(data, bookTitle);


        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    private void showBookData(BookData book, Author author) {

        String languageInSpanish;

        var languageInEnglish = book.languages().stream().map(LanguagesOptions::getNameByCode)
                .collect(Collectors.toList()).get(0);
        languageInSpanish = LanguagesOptions.getSpanishNameByCode(String.valueOf(languageInEnglish));

        System.out.println(


                "\n   Título: " + book.title() +
                        "\n   Autor: " + author.getName() +
                        "\n   Idioma: " + languageInSpanish +
                        "\n   Número de descargas: " + book.downloadCount()

        );
    }

    private void showBook(Book book, Author author) {

        String languageInSpanish;

        languageInSpanish = LanguagesOptions.getSpanishNameByCode(String.valueOf(book.getLanguage()));

        System.out.println(


                "\n   Título: " + book.getTitle() +
                        "\n   Autor: " + author.getName() +
                        "\n   Idioma: " + languageInSpanish +
                        "\n   Número de descargas: " + book.getDownloadCount()
        );
    }

    private void showAuthors(List<Author> authors) {
        List<String> authorBooks = new ArrayList<>();
        int count = 1;
        if (!authors.isEmpty()) {
            for (Author author : authors) {

                System.out.println("\n [" + count + "] \n" +
                        "------------------- Autor -------------------" +
                        author.toString());


                for (Book book : author.getBooks()) {
                    authorBooks.add(book.getTitle());
                }

                String booksString = String.join(", ", authorBooks);
                System.out.println("Books: " + booksString);
                System.out.println("\n------------------- ***** -------------------\n");

                count++;
            }
        } else {
            System.out.println("No se encontraron autores registrados en la base de datos.");
        }
    }

    private void saveBookData(BookData book, Author author) {

        Book newBook = new Book(book, author);
        bookRepository.save(newBook);
        System.out.println("--- Se ha guardado el libro " + book.title() + "en la base de datos. ---");

    }
}
