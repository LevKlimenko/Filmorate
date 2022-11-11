package ru.yandex.practicum.filmorate.storages.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genres findGenreById(Long id) {
        String sqlQuery = " select * from genres where id = ?";
        Genres genres;
        try {
            genres = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Genre с id " + id + " не найден");
        }
        return genres;
    }

    public List<Genres> findAllGenre() {
        String sqlQuery = "SELECT * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public List<Genres> findGenresOfFilm(Long id) {
        List<Genres> genres;
        try {
            String sqlQuery = "SELECT * FROM genres WHERE id IN(Select genre_id from film_genre where film_id = ?)";
            genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
        return genres;
    }

    private Genres mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genres.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
