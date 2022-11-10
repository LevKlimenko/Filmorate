package ru.yandex.practicum.filmorate.services.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class FilmDbService implements FilmLikeService {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmDbService(JdbcTemplate jdbcTemplate, @Qualifier("filmDbStorage") FilmStorage filmStorage, UserDbStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "MERGE INTO LIKES key(FILM_ID,USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE  from LIKES WHERE (FILM_ID=? and USER_ID=?)";
        jdbcTemplate.update(sqlQuery,filmId,userId);
    }

    @Override
    public List showMostLikedFilms(Integer count) {
        String sqlQuery = "SELECT film_id " +
                "From likes GROUP BY film_id ORDER BY count(user_id) DESC LIMIT ? ";

        return jdbcTemplate.queryForObject(sqlQuery,List.class, count);
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

    @Override
    public Map<Long, Film> getMap() {
        return filmStorage.getMap();
    }
}
