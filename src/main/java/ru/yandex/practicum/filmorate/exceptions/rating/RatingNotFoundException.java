package ru.yandex.practicum.filmorate.exceptions.rating;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String message) {
        super(message);
    }
}
