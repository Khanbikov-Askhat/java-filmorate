package ru.yandex.practicum.filmorate.service.implservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;


@Service
@Slf4j
public class FilmServiceImpl {

    private FilmDao filmDao;
    private MpaDao mpaDao;
    private UserDao userDao;
    private static final String GENRE_QUALIFIER = "genreDaoImpl";
    private GenreDao genreDao;
    private final JdbcTemplate jdbcTemplate;

    public FilmServiceImpl(JdbcTemplate jdbcTemplate, @Qualifier(GENRE_QUALIFIER) GenreDao genreDao) {
        this.genreDao = genreDao;
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Film> findAll() {
        final List<Film> films = filmDao.findAll();
        for (Film film: films) {
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<Genre>());
            }
        }
        load(films);
        return films;
    }

    public Film findById(Long filmId) {
        Optional<Film> foundedFilm = getFilmById(filmId);
        return foundedFilm.get();
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
        if (film.isPresent()) {
            film.get().setGenres(genreDao.getGenresByFilmId(filmId));
            film.get().getFilmLikes().addAll(new HashSet<>(filmDao.getLikesByFilmId(filmId)));
        }
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

    public void load(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sqlQuery = "SELECT * FROM FILM_GENRE fg, " + //"SELECT g.GENRE_ID, g.GENRE_NAME FROM FILM_GENRE AS fg " +
                "GENRE g WHERE fg.GENRE_ID = g.GENRE_ID AND" +
                " fg.film_id in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery,  (rs) -> {
            final Film film = filmById.get(rs.getLong("FILM_ID"));
            film.addGenre(makeGenre(rs, 0));
        }, films.stream().map(Film::getId).toArray());
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getLong("genre_id"),
                rs.getString("genre_name"));
    }

    @Autowired
    @Qualifier("filmDaoImpl")
    public void setFilmDao(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Autowired
    @Qualifier("ratingDaoImpl")
    public void setRatingDao(MpaDao ratingDao) {
        this.mpaDao = ratingDao;
    }

    @Autowired
    @Qualifier("userDaoImpl")
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
