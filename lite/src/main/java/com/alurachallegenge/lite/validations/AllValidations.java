package com.alurachallegenge.lite.validations;

import com.alurachallegenge.lite.exceptions.BookNotFoundException;
import com.alurachallegenge.lite.exceptions.InvalidOptionsException;
import com.alurachallegenge.lite.model.ResultsData;

public class AllValidations {


    //Input data validations for the main menu
    public static void verifyMenuInputIsValid(int input) throws Exception {
        if (input < 0 || input > 6) {
            throw new InvalidOptionsException("Opción inválida, intente de nuevo con las opciones disponibles en el menú.");
        }
    }

    // Data validation for Gutendex
    public static void verifyIsnotNullData(ResultsData data, String bookTitle) throws BookNotFoundException {
        if (data.results().isEmpty() || data.results() == null) {
            throw new BookNotFoundException("Lo sentimos, el libro con título " + bookTitle + " no se encontró.");
        }
    }

    //Input data validations for books in case we have multiple matches in the Gutendex search
    public static void verifyGutendexInputIsValid(int input, int elementsNumber) throws InvalidOptionsException {
        if (input < 0 || input > elementsNumber) {
            throw new InvalidOptionsException("Opción inválida, intente de nuevo con las opciones disponibles en el menú.");
        } else if (input == 0) {
            System.out.println("Programa finalizado. Cerrando aplicación...");
            System.exit(0);
        }
    }

    //Input data validation for year. Option 4 of main menu
    public static void verifyYearsFormat(int inputYear) throws InvalidOptionsException {
        if (inputYear < -4000 || inputYear > 2024) {
            throw new InvalidOptionsException("Opción inválida, intente con otros valores");

        }
    }

    //Input Data validation for searching books by language
    public static void verifyOptionsForLanguageMeu(int input) throws InvalidOptionsException {
        if (input > 7 || input < 1) {
            throw new InvalidOptionsException("Opción inválida, seleccione una opción del menú.");
        }
    }
}
