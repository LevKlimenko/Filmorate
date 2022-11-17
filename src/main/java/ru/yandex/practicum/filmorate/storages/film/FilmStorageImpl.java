package ru.yandex.practicum.filmorate.storages.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class FilmStorageImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * from films Inner JOIN MPA ON mpa.id=films.mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        if (film.getMpa() == null) {
            throw new DataIntegrityViolationException("MPA can't be null");
        }
        String sqlQuery = "insert into films(name,releaseDate,description,duration,rate,mpa) values(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setObject(2, film.getReleaseDate());
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setLong(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull((keyHolder.getKey()).longValue());
        film.setId(id);
        if (validateGenresNotNull(film)) {
            mergeFilmGenres(film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getMpa() == null) {
            throw new DataIntegrityViolationException("MPA can't be null");
        }
        isExist(film.getId());
        String sqlQuery = "UPDATE films set name = ?, releaseDate = ?, description =?, duration = ?,rate=?, mpa = ? " +
                "where id = ?";
        try {
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getReleaseDate()
                    , film.getDescription()
                    , film.getDuration()
                    , film.getRate()
                    , film.getMpa().getId()
                    , film.getId());
            if (validateGenresNotNull(film)) {
                deleteFilmGenres(film);
                mergeFilmGenres(film);
            }
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Film with id=%d not found.", film.getId()));
        }
    }

    @Override
    public Film findById(Long filmId) {
        String sqlQuery = "SELECT * FROM films INNER JOIN MPA ON mpa.id=films.mpa  where films.ID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Film with id=%d not found.", filmId));
        }
        return film;
    }

    @Override
    public boolean isExist(Long id) {
        if (findById(id) == null) {
            throw new NotFoundException(String.format("Film with id=%d not found.", id));
        }
        return true;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .releaseDate((resultSet.getDate("releaseDate")).toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa.id"))
                        .name(resultSet.getString("mpa.name"))
                        .build())
                .genres(new LinkedHashSet<>())
                .build();
    }

    private boolean validateGenresNotNull(Film film) {
        return film.getGenres() != null;
    }

    private void mergeFilmGenres(Film film) {
        final List<Genre> genreList = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate("MERGE INTO film_genre key(film_id, genre_id) values (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setLong(2, genreList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return film.getGenres().size();
            }
        });
    }

    private void deleteFilmGenres(Film film) {
        String sqlQuery = "DELETE FROM film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }
}