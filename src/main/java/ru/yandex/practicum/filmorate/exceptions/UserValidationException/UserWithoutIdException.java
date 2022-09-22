package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserWithoutIdException extends RuntimeException {
    public UserWithoutIdException(String message) {
        super(message);
    }
}
