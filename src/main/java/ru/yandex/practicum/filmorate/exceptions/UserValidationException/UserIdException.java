package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserIdException extends RuntimeException {
    public UserIdException(String message) {
        super(message);
    }
}
