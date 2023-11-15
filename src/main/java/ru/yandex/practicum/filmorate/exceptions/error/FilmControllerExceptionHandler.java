package ru.yandex.practicum.filmorate.exceptions.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controllers.implcontrollers.FilmImplController;
import ru.yandex.practicum.filmorate.controllers.implcontrollers.GenreImplController;
import ru.yandex.practicum.filmorate.controllers.implcontrollers.MpaImplController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.film.LikeNotAddedException;
import ru.yandex.practicum.filmorate.exceptions.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.rating.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice(assignableTypes = {FilmImplController.class, GenreImplController.class, MpaImplController.class})
public class FilmControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler({LikeNotAddedException.class,
            FilmNotExistException.class,
            UserNotExistException.class,
            RatingNotFoundException.class,
            GenreNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return new ErrorResponse("Ошибка поиска", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse("Server error",
                "An unexpected error has occurred " + e.getMessage());
    }
}
