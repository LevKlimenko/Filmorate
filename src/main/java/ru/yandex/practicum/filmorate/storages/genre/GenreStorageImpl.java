package ru.yandex.practicum.filmorate.storages.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Repository
@RequiredArgsConstructor
public class GenreStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(Long id) {
        String sqlQuery = " select * from genres where id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Genre with id=%d not found.", id));
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public void load(List<Film> films) {
        final Map<Long, Film> filmbyId = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        List<Film> film = jdbcTemplate.query(String.format("Select GENRE_ID from Film_genre WHERE FILM_id IN (%s)", inSql ),films.toArray(),
                (rs, rowNum) -> filmbyId.get(rs.getLong("Film_ID")));
    }

}