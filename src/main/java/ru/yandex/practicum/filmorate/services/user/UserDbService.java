package ru.yandex.practicum.filmorate.services.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserDbService implements UserFriendService {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public UserDbService(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public void becomeFriend(Long userId1, Long userId2) {
        if (userDbStorage.isExist(userId1) && userDbStorage.isExist(userId2)) {
            if (userId1.equals(userId2)) {
                throw new ConflictException("Сan't add yourself as a friend");
            }
            User user = findById(userId1);
            if (user.getFriendsId().contains(userId2)) {
                throw new ConflictException("User id:" + userId1 + " and user id:" + userId2 + " already friends");
            }
            String sqlQuery = "MERGE INTO FRIENDSHIP key(USER_ID, FRIEND_ID) values(?,?)";
            jdbcTemplate.update(sqlQuery, userId1, userId2);
        }
    }

    @Override
    public void stopBeingFriends(Long userId1, Long userId2) {
        if (userDbStorage.isExist(userId1) && userDbStorage.isExist(userId2)) {
            String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID=? and FRIEND_ID=?";
            int row = jdbcTemplate.update(sqlQuery, userId1, userId2);
            if (row == 0) {
                throw new ConflictException("Users ain't friends ");
            }
        }
    }

    @Override
    public List<User> showAllUserFriends(Long userId) {
        userDbStorage.isExist(userId);
        String sqlQuery = "SELECT ID From Users u WHERE ID in (SELECT FRIEND_ID FROM FRIENDSHIP where USER_ID = ? )";
        List<Long> filmRows = jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
        return userDbStorage.getUsers(filmRows);
    }

    @Override
    public List<User> showIntersectionFriends(Long userId1, Long userId2) {
        userDbStorage.isExist(userId1);
        userDbStorage.isExist(userId2);
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID=? INTERSECT ( SELECT FRIEND_ID FROM " +
                "FRIENDSHIP WHERE USER_ID=?)";
        List<Long> filmRows = jdbcTemplate.queryForList(sqlQuery, Long.class, userId1, userId2);
        return userDbStorage.getUsers(filmRows);
    }

    @Override
    public Collection<User> getAll() {
        return userDbStorage.getAll();
    }

    @Override
    public User create(User user) {
        checkBlankAndNullName(user);
        return userDbStorage.create(user);
    }

    @Override
    public User update(User user) {
        checkBlankAndNullName(user);
        return userDbStorage.update(user);
    }

    @Override
    public User findById(Long id) {
        return userDbStorage.findById(id);
    }

    private void checkBlankAndNullName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}