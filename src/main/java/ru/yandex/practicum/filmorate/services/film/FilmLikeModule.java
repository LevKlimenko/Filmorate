package ru.yandex.practicum.filmorate.services.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.List;

@Service
public class FilmLikeModule implements FilmLikeService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmLikeModule(JdbcTemplate jdbcTemplate, FilmStorage filmStorage, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmStorage.isExist(filmId);
        userStorage.isExist(userId);
        String sqlQuery = "MERGE INTO LIKES key(FILM_ID,USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        filmStorage.isExist(filmId);
        userStorage.isExist(userId);
        String sqlQuery = "DELETE from LIKES WHERE FILM_ID=? AND USER_ID=?";
        int row = jdbcTemplate.update(sqlQuery, filmId, userId);
        if (row == 0) {
            throw new NotFoundException("User like for film not found. filmId:" + filmId + " userId:" + userId);
        }
        return true;
    }

    @Override
    public List<Film> showMostLikedFilms(Integer count) {
        String sqlQuery = "SELECT f.ID, count(DISTINCT LIKES.USER_ID) as cnt " +
                "From FILMS f LEFT OUTER JOIN LIKES on F.ID = LIKES.FILM_ID " +
                "GROUP BY f.ID " +
                "ORDER BY cnt DESC LIMIT ? ";
        List<Long> filmsRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("ID"), count);
        return filmStorage.getFilms(filmsRows);
    }
}
