package ru.yandex.practicum.filmorate.storages.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.constants.FilmGenre;

import java.sql.*;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Qualifier("filmDbStorage")
@Component
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Film> getCompare() {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return null;
    }
    @Qualifier
    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into FILMS(NAME,RELEASE_DATE,DURATION,DESCRIPTION,MPA) values(?,?,?,?,?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3,film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, Math.toIntExact(film.getMpa().getId()));
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull((keyHolder.getKey()).longValue());

        if (film.getGenre() != null){
            for(Genre genre: film.getGenre()){
                String sql = "insert into FILM_GENRE values (?, ?)";
                jdbcTemplate.update(sql,
                        id,
                        genre.getId());
            }
        }
        return findById(id);
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film findById(Long filmId) {
        String sqlQuery = "SELECT * FROM films where id = ?";
        Film film;
        try{
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e){
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

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException{
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .releaseDate((resultSet.getDate("release_date")).toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                //.mpa(mpaService.findMpaById(Long.valueOf((resultSet.getString("mpa")))))
                //.genres(genreService.findFilmFenresByFilmId(resultSet.getLong("id")))
                .build();

    }
}
