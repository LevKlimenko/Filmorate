package ru.yandex.practicum.filmorate.services.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class UserDbService implements UserFriendService {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public UserDbService(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public boolean becomeFriend(Long userId1, Long userId2) {
        if (userId1.equals(userId2)) {
            throw new ConflictException("Нельзя добавить себя в друзья");
        }
        if (findById(userId1) != null && findById(userId2) != null) {
            String sqlQuery = "MERGE INTO FRIENDSHIP key(USER_ID, FRIEND_ID) values(?,?)";
            return jdbcTemplate.update(sqlQuery, userId1, userId2) > 0;
        }
        return false;
    }

    @Override
    public boolean stopBeingFriends(Long userId1, Long userId2) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID=? and FRIEND_ID=?";
        return jdbcTemplate.update(sqlQuery, userId1, userId2) > 0;
    }

    @Override
    public List<User> showAllUserFriends(Long userId) {
        String sqlQuery = "SELECT ID From Users u WHERE ID in (SELECT FRIEND_ID FROM FRIENDSHIP where USER_ID = ? )";
        List<Long> filmRows = jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
        return userDbStorage.getUsers(filmRows);
    }

    @Override
    public List<User> showIntersectionFriends(Long userId1, Long userId2) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID=? INTERSECT ( SELECT FRIEND_ID FROM " +
                "FRIENDSHIP WHERE USER_ID=?)";
        List<Long> filmRows = jdbcTemplate.queryForList(sqlQuery, Long.class, userId1, userId2);
        return userDbStorage.getUsers(filmRows);
    }

    @Override
    public Collection<User> getAll() {
        return userDbStorage.getUser();
    }

    @Override
    public User create(User user) {
        return userDbStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userDbStorage.update(user);
    }

    @Override
    public User findById(Long id) {
        return userDbStorage.findById(id);
    }

    @Override
    public Map<Long, User> getMap() {
        return userDbStorage.getMap();
    }
}
