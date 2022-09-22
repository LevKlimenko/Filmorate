package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserWithoutEmailException extends RuntimeException {
    public UserWithoutEmailException(String message) {
        super(message);
    }
}
