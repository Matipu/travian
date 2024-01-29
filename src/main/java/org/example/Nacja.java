package org.example;

public enum Nacja {

    GERMANIE("germanie"), GALOWIE("galowie"), RZYMIANIE("rzymianie");

    public String nazwa;

    Nacja(String nazwa) {
        this.nazwa = nazwa;
    }

    public static Nacja getNacja(String nazwa) {
        return switch (nazwa) {
            case "germanie" -> GERMANIE;
            case "galowie" -> GALOWIE;
            case "rzymianie" -> RZYMIANIE;
            default -> null;
        };
    }
}
