package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmBadDescriptionException extends RuntimeException {
    public FilmBadDescriptionException(String message) {
        super(message);
    }
}
