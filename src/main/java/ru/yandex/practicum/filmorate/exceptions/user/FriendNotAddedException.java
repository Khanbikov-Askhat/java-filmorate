package ru.yandex.practicum.filmorate.exceptions.user;

public class FriendNotAddedException extends RuntimeException {
    public FriendNotAddedException(String message) {
        super(message);
    }
}
