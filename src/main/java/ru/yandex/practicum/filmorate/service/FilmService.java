package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.film.LikeNotAddedException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private InMemoryFilmStorage inMemoryFilmStorage;
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }


    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film film) {
        return inMemoryFilmStorage.update(film);
    }

    public Film getFilmById(Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    public Film addLike(Long filmId, Long userId) {
        isFilmExist(filmId);
        isUserExist(userId);
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        if (film.setFilmLikes(filmId)) {
            return film;
        } else {
            throw new LikeNotAddedException("Лайк пользователя с id " + userId + " не был добавлен");
        }
    }

    public Film deleteLike(Long filmId, Long userId) {
        isFilmExist(filmId);
        isUserExist(userId);
        Film film = inMemoryFilmStorage.getFilmById(userId);
        film.removeLike(userId);
        return film;
    }

    public List<Film> getSortedFilmsByLikes(Long count) {
        return inMemoryFilmStorage.findAll().stream()
                .sorted(((o1, o2) -> o2.getFilmLikes().size() - o1.getFilmLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void isFilmExist(Long filmId) {
        if (!inMemoryFilmStorage.isFilmExist(filmId)) {
            throw new FilmNotExistException("фильма с id " + filmId + " не существует");
        }
    }

    public void isUserExist(Long userId) {
        if (!inMemoryUserStorage.isUserExist(userId)) {
            throw new UserNotExistException("Пользователя с id " + userId + " не существует");
        }
    }
}
