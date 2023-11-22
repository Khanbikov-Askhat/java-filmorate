package ru.yandex.practicum.filmorate.service.implservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.classdao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.classdao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.classdao.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.classdao.UserDao;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;




@Service
@Slf4j
public class FilmServiceImpl {

    private static final String GENRE_QUALIFIER = "genreDaoImpl";
    private static final String FILM_QUALIFIER = "filmDaoImpl";
    private static final String USER_QUALIFIER = "userDaoImpl";
    private static final String MPA_QUALIFIER = "mpaDaoImpl";
    private final FilmDao filmDao;
    private final MpaDao mpaDao;
    private final UserDao userDao;
    private final GenreDao genreDao;

    public FilmServiceImpl(@Qualifier(FILM_QUALIFIER) FilmDao filmDao, @Qualifier(MPA_QUALIFIER) MpaDao mpaDao,
                           @Qualifier(USER_QUALIFIER) UserDao userDao, @Qualifier(GENRE_QUALIFIER) GenreDao genreDao) {
        this.filmDao = filmDao;
        this.mpaDao = mpaDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
    }


    public List<Film> findAll() {
        final List<Film> films = filmDao.findAll();
        for (Film film: films) {
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<Genre>());
            }
        }
        genreDao.load(films);
        return films;
    }


    public Film findById(Long filmId) {

        Optional<Film> foundedFilm = getFilmById(filmId);
        Film film = foundedFilm.get();
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<Genre>());
        }
        genreDao.load((Collections.singletonList(film)));
        return film;
    }


    public Film save(Film film) {
        filmValidation(film);
        return filmDao.save(film);
    }


    public Film update(Film film) {
        filmValidation(film);
        filmExistsValidation(film);
        setGenresToFilm(film);
        return filmDao.update(film).get();
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
        if (film.getId() < 0 || getFilmById(film.getId()).isEmpty()) {
            throw new FilmNotExistException("Фильм  с id: " + film.getId() + " не найден");
        }
    }

    private void filmIdExistsValidation(Long filmId) {
        if (getFilmById(filmId).isEmpty()) {
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
            mpaDao.getRatingById(film.getMpa().getId());
        } catch (DataAccessException e) {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
    }

    public Optional<Film> getFilmById(Long filmId) {
        Optional<Film> film = filmDao.getValidFilmByFilmId(filmId);
        return film;
    }

    private void setGenresToFilm(Film film) {
        try {
            List<Genre> genresFromDbByFilm = genreDao.getGenresIdByFilmId(film.getId());
            if (!film.getGenres().isEmpty()) {
                List<Genre> genresFromUI = new ArrayList<>(film.getGenres());
                if (genresFromDbByFilm.isEmpty()) {
                    filmDao.updateFilmGenres(genresFromUI, film);
                } else {
                    List<Genre> matchedGenres = filmDao.getGenresMatch(genresFromUI, genresFromDbByFilm);
                    genresFromUI.removeAll(matchedGenres);
                    genresFromDbByFilm.removeAll(matchedGenres);
                    filmDao.deleteFilmGenresFromDb(genresFromDbByFilm);
                    filmDao.updateFilmGenres(genresFromUI, film);
                }
            } else {
                filmDao.deleteFilmGenresFromDb(genresFromDbByFilm);
            }
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Жанр не найден" + e.getMessage());
        }
    }


}
