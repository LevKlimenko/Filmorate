package ru.yandex.practicum.filmorate.storages.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserFriendDbStorageImpl implements UserFriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean becomeFriend(Long userId1, Long userId2) {
        if (userId1.equals(userId2)) {
            throw new ConflictException("Сan't add yourself as a friend");
        }
        isExistUser(userId1);
        isExistUser(userId2);
        String sqlQuery = "MERGE INTO FRIENDSHIP key(USER_ID, FRIEND_ID) values(?,?)";
        int row = jdbcTemplate.update(sqlQuery, userId1, userId2);
        if (row == 0) {
            throw new NotFoundException("Users not found");
        }
        return true;
    }

    @Override
    public boolean stopBeingFriends(Long userId1, Long userId2) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID=? and FRIEND_ID=?";
        int row = jdbcTemplate.update(sqlQuery, userId1, userId2);
        if (row == 0) {
            throw new NotFoundException("Users ain't friends ");
        }
        return true;
    }

    @Override
    public List<User> showAllUserFriends(Long userId) {
        isExistUser(userId);
        String sqlQuery = "SELECT id, email, login, name, birthday FROM FRIENDSHIP " +
                "INNER JOIN USERS as u ON u.ID = FRIEND_ID where USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public List<User> showIntersectionFriends(Long userId1, Long userId2) {
        isExistUser(userId1);
        isExistUser(userId2);
        String sqlQuery = "select * from USERS u, FRIENDSHIP f, FRIENDSHIP o " +
                "where u.ID = f.FRIEND_ID AND u.ID = o.FRIEND_ID AND f.USER_ID = ? AND o.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId1, userId2);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email((resultSet.getString("email")))
                .login((resultSet.getString("login")))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private void isExistUser(Long id) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT count(*) From USERS where id = ?", Integer.class, id);
        if (cnt == null || cnt <= 0) {
            throw new NotFoundException("User with ID=" + id + " not found");
        }
    }
}