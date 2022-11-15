package ru.yandex.practicum.filmorate.services.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;

import java.util.Collection;

@Service
public class FilmDbService implements CrudService<Film> {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmDbService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film findById(Long id) {
        return filmStorage.findById(id);
    }
}