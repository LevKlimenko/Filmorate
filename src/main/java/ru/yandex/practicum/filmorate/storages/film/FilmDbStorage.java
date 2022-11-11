package ru.yandex.practicum.filmorate.storages.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genres;
import ru.yandex.practicum.filmorate.services.genres.GenreService;
import ru.yandex.practicum.filmorate.services.mpa.MpaService;

import java.sql.Date;
import java.sql.*;
import java.util.*;

@Qualifier("filmDbStorage")
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    @Override
    public Set<Film> getCompare() {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films(name,releaseDate,description,duration,rate,mpa) values(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull((keyHolder.getKey()).longValue());

        if (film.getGenres() != null) {
            for (Genres genres : film.getGenres()) {
                String sql = "insert into FILM_GENRE values (?, ?)";
                jdbcTemplate.update(sql,
                        id,
                        genres.getId());
            }
        }

        return findById(id);
    }

    @Override
    public Film update(Film film) {
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
            if (film.getGenres() != null) {
                sqlQuery = "DELETE FROM film_genre where film_id = ?";
                jdbcTemplate.update(sqlQuery, film.getId());
                for (Genres genres :
                        film.getGenres()) {
                    sqlQuery = "MERGE INTO film_genre key(film_id, genre_id) values (?, ?)";
                    jdbcTemplate.update(sqlQuery, film.getId(), genres.getId());
                }
            }
            return findById(film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Фильм с id=%d не найден.", film.getId()));
        }
    }


    @Override
    public Film findById(Long filmId) {
        String sqlQuery = "SELECT * FROM films where id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Фильм с id=%d не найден.", filmId));
        }
        return film;
    }

    @Override
    public Map<Long, Film> getMap() {
        return null;
    }

    @Override
    public boolean isExist(Long id) {
        return false;
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .releaseDate((resultSet.getDate("releaseDate")).toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(mpaService.findById(Long.valueOf((resultSet.getString("mpa")))))
                .genres(genreService.findGenresOfFilm(Long.valueOf(resultSet.getString(("id")))))
                .likesId(findUsersIdWhoLikedFilm(resultSet.getLong("id")))
                .build();
    }

    @Override
    public List<Film> getFilms(List<Long> filmsId) {
        String inSql = String.join(",", Collections.nCopies(filmsId.size(), "?"));
        return jdbcTemplate.query(String.format("SELECT * FROM films WHERE id in (%s)", inSql),
                this::mapRowToFilm,
                filmsId.toArray());
    }


    private List<Long> findUsersIdWhoLikedFilm(Long id) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, id);
    }

}