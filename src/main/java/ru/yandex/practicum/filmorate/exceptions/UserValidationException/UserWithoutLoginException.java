package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserWithoutLoginException extends RuntimeException {
    public UserWithoutLoginException(String message) {
        super(message);
    }
}
