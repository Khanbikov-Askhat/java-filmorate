package ru.yandex.practicum.filmorate.service.implservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.rating.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.RatingDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl {

    private FilmDao filmDao;
    private RatingDao ratingDao;
    private UserDao userDao;


    public List<Film> findAll() {
        return filmDao.findAll();
    }


    public Optional<Film> findById(Long filmId) {
        return filmDao.getFilmById(filmId);
    }


    public Optional<Film> save(Film film) {
        filmValidation(film);
        return filmDao.save(film);
    }


    public Optional<Film> update(Film film) {
        filmValidation(film);
        filmExistsValidation(film);
        return filmDao.update(film);
    }


    public void delete(Long filmId) {
        filmIdExistsValidation(filmId);
        filmDao.delete(filmId);
    }

    public void addLike(long filmId, long userId) {
        filmIdExistsValidation(filmId);
        userIdExistsValidation(userId);
        filmDao.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        filmIdExistsValidation(filmId);
        userIdExistsValidation(userId);
        filmDao.deleteLike(filmId, userId);
    }

    public List<Film> getSortedFilmsByLikes(Long count) {
        if (count < 0) {
            throw new FilmNotExistException("Не правильное количество популярных фильмов");
        }
        if (count == 0) {
            count = 10L;
        }
        return filmDao.getSortedFilmsByLikes(count);
    }

    private void filmExistsValidation(Film film) {
        if (film.getId() < 0 || filmDao.getFilmById(film.getId()).isEmpty()) {
            throw new FilmNotExistException("Фильм  с id: " + film.getId() + " не найден");
        }
    }

    private void filmIdExistsValidation(Long filmId) {
        if (filmDao.getFilmById(filmId).isEmpty()) {
            throw new FilmNotExistException("Фильм  с id: " + filmId + " не найден");
        }
    }

    private void userIdExistsValidation(Long userId) {
        if (userId < 0 || userDao.getUserById(userId).isEmpty()) {
            throw new UserNotExistException("Пользователь с id: " + userId + " не существует");
        }
    }

    private void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film must be released after 27.12.1895");
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Film must have a name");
        }
        if (film.getMpa() == null) {
            throw new ValidationException("Пустой рейтинг");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Неверная дата релиза");
        }
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        } else {
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        try {
            ratingDao.getRatingById(film.getMpa().getId());
        } catch (DataAccessException e) {
            throw new RatingNotFoundException("Рейтинг не найден");
        }
    }

    @Autowired
    @Qualifier("filmDaoImpl")
    public void setFilmDao(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Autowired
    @Qualifier("ratingDaoImpl")
    public void setRatingDao(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    @Autowired
    @Qualifier("userDaoImpl")
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
