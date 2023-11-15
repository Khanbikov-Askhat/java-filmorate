package ru.yandex.practicum.filmorate.exceptions.film;

public class FilmorateAlreadyExistsException extends RuntimeException {

    public FilmorateAlreadyExistsException(String message) {
        super(message);
    }
}
