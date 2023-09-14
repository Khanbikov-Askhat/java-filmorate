package ru.yandex.practicum.filmorate.exceptions.user;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String message) {
        super(message);
    }
}