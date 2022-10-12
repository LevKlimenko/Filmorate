package ru.yandex.practicum.filmorate.exceptions;

public class UsersAlreadyFriendsException extends RuntimeException {
    public UsersAlreadyFriendsException(String message) {
        super(message);
    }
}
