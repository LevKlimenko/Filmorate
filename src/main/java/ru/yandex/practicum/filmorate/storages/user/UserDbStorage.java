package ru.yandex.practicum.filmorate.storages.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users(login, name,email, birthday) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return findById(id);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new BadRequestException("Can't update user without ID");
        }
        isExist(user.getId());
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday =?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return findById(user.getId());
    }

    @Override
    public User findById(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE  id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User with id=%d not found.", id));
        }
        return user;
    }

    @Override
    public boolean isExist(Long id) {
        if (findById(id) == null) {
            throw new NotFoundException(String.format("User with id=%d not found.", id));
        }
        return true;
    }

    public List<User> getUsers(List<Long> userId) {
        String inSql = String.join(",", Collections.nCopies(userId.size(), "?"));
        return jdbcTemplate.query(String.format("SELECT * FROM users WHERE id in (%s)", inSql),
                this::mapRowToUser,
                userId.toArray());
    }


    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email((resultSet.getString("email")))
                .login((resultSet.getString("login")))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friendsId(findUsersFriends(resultSet.getLong("id")))
                .build();
    }

    private List<Long> findUsersFriends(Long id) {
        String sqlQuery = "SELECT FRIEND_ID FROM friendship WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, id);
    }
}
