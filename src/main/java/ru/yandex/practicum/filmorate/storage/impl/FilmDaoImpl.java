package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmorateAlreadyExistsException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.sqloperation.FilmSqlOperation.*;

@Repository
public class FilmDaoImpl implements FilmDao {
    private static final String FILM_TABLE_NAME = "films";
    private static final String FILM_TABLE_ID_COLUMN_NAME = "film_id";
    private static final String GENRE_QUALIFIER = "genreDaoImpl";
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate, @Qualifier(GENRE_QUALIFIER) GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public List<Film> findAll() {
        List<Film> foundedFilm = jdbcTemplate.query(GET_ALL_FILMS.getTitle(), new FilmMapper());
        for (Film film : foundedFilm) {
            if (film.getGenres() == null) {
                film.setGenres(genreDao.getGenresByFilmId(film.getId()));
            }
        }
        return foundedFilm;
    }

    @Override
    public Optional<Film> save(Film film) {
        filmInsertAndSetId(film);
        addGenresToFilm(film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        jdbcTemplate.update(UPDATE_FILM.getTitle(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        setGenresToFilm(film);
        return Optional.of(film);
    }

    @Override
    public void delete(Long filmId) {
        try {
            jdbcTemplate.update(DELETE_FILM.getTitle(), filmId);
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Фильм не найден" + e.getMessage());
        }
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        Optional<Film> film = getValidFilmByFilmId(filmId);
        if (film.isPresent()) {
            film.get().setGenres(genreDao.getGenresByFilmId(filmId));
            film.get().getFilmLikes().addAll(new HashSet<>(getLikesByFilmId(filmId)));
        }
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (isLikeExistsInFilm(filmId, userId)) {
            throw new FilmorateAlreadyExistsException("Лайк пользователя " + filmId + " уже стоит");
        }
        jdbcTemplate.update(CREATE_LIKE.getTitle(), filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(DELETE_LIKE.getTitle(), filmId, userId);
    }

    @Override
    public List<Film> getSortedFilmsByLikes(Long count) {
        List<Film> sortedFilm = jdbcTemplate.query(GET_MOST_POPULAR_FILMS.getTitle(), new FilmMapper(), count);
        for (Film film : sortedFilm) {
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<Genre>());
            }
        }
        return sortedFilm;
    }

    @Override
    public List<Long> getLikesByFilmId(long filmId) {
        return jdbcTemplate.queryForList(GET_USER_LIKES_BY_FILM_ID.getTitle(), Long.class, filmId);
    }

    private Optional<Film> getValidFilmByFilmId(Long filmId) {
        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(GET_FILM_BY_FILM_ID.getTitle(), new FilmMapper(), filmId));
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Фильм не найден" + e.getMessage());
        }
    }

    private boolean isLikeExistsInFilm(long filmId, long userId) {
        return getLikesByFilmId(filmId).contains(userId);
    }

    private void filmInsertAndSetId(Film film) {
        long filmId = getFilmSimpleJdbcInsert().executeAndReturnKey(film.toMap()).longValue();
        film.setId(filmId);
    }

    private SimpleJdbcInsert getFilmSimpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILM_TABLE_NAME)
                .usingGeneratedKeyColumns(FILM_TABLE_ID_COLUMN_NAME);
    }

    private void addGenresToFilm(Film film) {
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(CREATE_FILM_GENRE.getTitle(), film.getId(), genre.getId());
            }
        }
    }

    private void setGenresToFilm(Film film) {
        try {
            List<Genre> genresFromDbByFilm = genreDao.getGenresIdByFilmId(film.getId());
            if (!film.getGenres().isEmpty()) {
                List<Genre> genresFromUI = new ArrayList<>(film.getGenres());
                if (genresFromDbByFilm.isEmpty()) {
                    updateFilmGenres(genresFromUI, film);
                } else {
                    List<Genre> matchedGenres = getGenresMatch(genresFromUI, genresFromDbByFilm);
                    genresFromUI.removeAll(matchedGenres);
                    genresFromDbByFilm.removeAll(matchedGenres);
                    deleteFilmGenresFromDb(genresFromDbByFilm);
                    updateFilmGenres(genresFromUI, film);
                }
            } else {
                deleteFilmGenresFromDb(genresFromDbByFilm);
            }
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Жанр не найден" + e.getMessage());
        }
    }

    private List<Genre> getGenresMatch(List<Genre> genres1, List<Genre> genres2) {
        return genres1.stream()
                .filter(front -> genres2.stream().anyMatch(db -> db.getId() == front.getId()))
                .collect(Collectors.toList());
    }

    private void deleteFilmGenresFromDb(List<Genre> genres) {
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(DELETE_FILM_GENRES_BY_GENRE_ID.getTitle(), genre.getId());
            }
        }
    }

    private void updateFilmGenres(List<Genre> genresFromFrontEnd, Film film) {
        if (!genresFromFrontEnd.isEmpty()) {
            jdbcTemplate.batchUpdate(CREATE_FILM_GENRE.getTitle(), new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = genresFromFrontEnd.get(i);
                    ps.setLong(1, film.getId());
                    ps.setLong(2, genre.getId());
                }

                @Override
                public int getBatchSize() {
                    return genresFromFrontEnd.size();
                }
            });
        }
    }
}
