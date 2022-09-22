package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmLengthException extends RuntimeException {
    public FilmLengthException(String message) {
        super(message);
    }
}
