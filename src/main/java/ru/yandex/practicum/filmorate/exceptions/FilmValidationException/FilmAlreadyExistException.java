package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String message) {
        super(message);
    }
}
