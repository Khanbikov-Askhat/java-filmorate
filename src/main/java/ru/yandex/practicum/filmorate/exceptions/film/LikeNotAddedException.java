package ru.yandex.practicum.filmorate.exceptions.film;

public class LikeNotAddedException extends RuntimeException {
    public LikeNotAddedException(String message) {
        super(message);
    }
}
