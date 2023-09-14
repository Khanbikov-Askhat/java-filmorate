package ru.yandex.practicum.filmorate.exceptions.film;

public class FilmNotExistException extends RuntimeException {
    public FilmNotExistException(String message) {
        super(message);
    }
}
