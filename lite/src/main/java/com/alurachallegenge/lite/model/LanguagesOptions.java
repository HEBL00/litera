package com.alurachallegenge.lite.model;

public enum LanguagesOptions {
    ENGLISH("en", "inglés"),
    GERMAN("de", "alemán"),
    FRENCH("fr", "francés"),
    SPANISH("es", "español");

    private String languageCode;
    private String languageName;

    LanguagesOptions(String languageCode, String languageSpanish) {
        this.languageCode = languageCode;
        this.languageName = languageSpanish;
    }


    public static String getSpanishNameByCode(String enumName) {
        try {
            LanguagesOptions language = LanguagesOptions.valueOf(enumName.toUpperCase());
            return language.languageName;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Idioma no encontrado: " + enumName, e);
        }
    }

    public static LanguagesOptions getNameByCode(String code) {
        for (LanguagesOptions language : LanguagesOptions.values()) {
            if (language.languageCode.equalsIgnoreCase(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Idioma no encontrado: " + code);
    }


}

