package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmReleaseDateException extends RuntimeException {
    public FilmReleaseDateException(String message) {
        super(message);
    }
}
