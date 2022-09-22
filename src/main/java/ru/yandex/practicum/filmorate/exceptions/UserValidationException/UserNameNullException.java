package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserNameNullException extends RuntimeException {
    public UserNameNullException(String message) {
        super(message);
    }
}
