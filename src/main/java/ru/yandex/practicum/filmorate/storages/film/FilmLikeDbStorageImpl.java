package ru.yandex.practicum.filmorate.storages.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmLikeDbStorageImpl implements FilmLikeDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Override
    public boolean addLike(Long filmId, Long userId) {
        isExistFilm(filmId);
        isExistUser(userId);
        String sqlQuery = "MERGE INTO LIKES key(FILM_ID,USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return true;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        isExistFilm(filmId);
        isExistUser(userId);
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

    private void isExistFilm(Long id) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT count(*) From FILMS where id = ?", Integer.class, id);
        if (cnt == null || cnt <= 0) {
            throw new NotFoundException("Film with ID=" + id + " not found");
        }
    }

    private void isExistUser(Long id) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT count(*) From USERS where id = ?", Integer.class, id);
        if (cnt == null || cnt <= 0) {
            throw new NotFoundException("User with ID=" + id + " not found");
        }
    }
}
