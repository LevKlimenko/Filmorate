package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmNullNameException extends RuntimeException {
    public FilmNullNameException(String message) {
        super(message);
    }
}
