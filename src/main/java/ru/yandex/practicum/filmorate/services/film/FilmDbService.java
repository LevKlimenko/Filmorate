package ru.yandex.practicum.filmorate.services.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class FilmDbService implements FilmLikeService {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmDbService(JdbcTemplate jdbcTemplate, @Qualifier("filmDbStorage") FilmDbStorage filmStorage, UserDbStorage userStorage) {
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
        String sqlQuery = "DELETE from LIKES WHERE USER_ID IN (SELECT ID FROM USERS where ID=?)  AND FILM_ID " +
                "IN (SELECT ID FROM FILMS where ID=?)";
        jdbcTemplate.update(sqlQuery,userId,filmId);
    }




    @Override
    public List<Film> showMostLikedFilms(Integer count) {
        String sqlQuery = "SELECT f.ID, count(DISTINCT LIKES.USER_ID) as cnt " +
                "From FILMS f LEFT JOIN LIKES on F.ID = LIKES.FILM_ID " +
                "GROUP BY f.ID " +
                                    "ORDER BY cnt DESC LIMIT ? ";
        List<Long> filmsRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("id"), count);

        return filmStorage.getFilms(filmsRows);
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
