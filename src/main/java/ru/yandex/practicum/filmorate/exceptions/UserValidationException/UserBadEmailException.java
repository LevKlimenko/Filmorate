package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserBadEmailException extends RuntimeException {
    public UserBadEmailException(String message) {
        super(message);
    }
}
