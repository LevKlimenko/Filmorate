package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
