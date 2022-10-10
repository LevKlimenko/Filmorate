package ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

public class FilmNotFoundException extends IllegalStateException {
    public FilmNotFoundException(String s) {
        super(s);
    }
}
