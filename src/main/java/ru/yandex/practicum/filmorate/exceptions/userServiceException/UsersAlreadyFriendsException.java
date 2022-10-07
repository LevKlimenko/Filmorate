package ru.yandex.practicum.filmorate.exceptions.userServiceException;

public class UsersAlreadyFriendsException extends RuntimeException {
    public UsersAlreadyFriendsException(String message){
        super(message);
    }
}
