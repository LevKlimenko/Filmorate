package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserNullLoginException extends RuntimeException {
    public UserNullLoginException(String message) {
        super(message);
    }
}
