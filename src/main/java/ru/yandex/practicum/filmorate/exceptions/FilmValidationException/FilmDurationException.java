package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmDurationException extends RuntimeException {
    public FilmDurationException(String message) {
        super(message);
    }
}
