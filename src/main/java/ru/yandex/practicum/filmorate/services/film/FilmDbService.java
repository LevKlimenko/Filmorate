package ru.yandex.practicum.filmorate.services.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.storages.film.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.genre.GenreStorage;

import java.util.List;

@Service
public class FilmDbService implements CrudService<Film>, FilmLikeDbService {

    private final FilmStorage filmStorage;
    private final FilmLikeDbStorage filmLikeDbStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbService(FilmStorage filmStorage, FilmLikeDbStorage filmLikeDbStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Film> getAll() {
        final List<Film> films = filmStorage.getAll();
        genreStorage.load(films);
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
        final List<Film> films = filmStorage.getAll();
        genreStorage.load(films);
        return filmStorage.findById(id);
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        return filmLikeDbStorage.addLike(filmId, userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        return filmLikeDbStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> showMostLikedFilms(Integer count) {
        final List<Film> films = filmStorage.getAll();
        genreStorage.load(films);
        return filmLikeDbStorage.showMostLikedFilms(count);
    }
}