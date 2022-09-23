package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserBadLoginException extends RuntimeException {
    public UserBadLoginException(String message) {
        super(message);
    }
}
