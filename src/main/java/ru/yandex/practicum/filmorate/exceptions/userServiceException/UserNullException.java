package ru.yandex.practicum.filmorate.exceptions.userServiceException;

public class UserNullException extends NullPointerException{
    public UserNullException(String s) {
        super(s);
    }
}
