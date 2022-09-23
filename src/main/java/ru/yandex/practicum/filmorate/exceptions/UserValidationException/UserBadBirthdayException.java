package ru.yandex.practicum.filmorate.exceptions.UserValidationException;

public class UserBadBirthdayException extends RuntimeException {
    public UserBadBirthdayException(String message) {
        super(message);
    }
}
