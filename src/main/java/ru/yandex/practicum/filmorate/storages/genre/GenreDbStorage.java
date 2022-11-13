package ru.yandex.practicum.filmorate.storages.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre findGenreById(Long id) {
        String sqlQuery = " select * from genres where id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Genre with id=%d not found.", id));
        }
        return genre;
    }

    public List<Genre> findAllGenre() {
        String sqlQuery = "SELECT * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public List<Genre> findGenresOfFilm(Long id) {
        List<Genre> genres;
        try {
            String sqlQuery = "SELECT * FROM genres WHERE id IN(Select genre_id from film_genre where film_id = ?)";
            genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Genre with id=%d not found.", id));
        }
        return genres;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
