package ru.yandex.practicum.filmorate.storage.impl;

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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.sqloperation.FilmSqlOperation.*;

@Repository
public class FilmDaoImpl implements FilmDao {
    private static final String FILM_TABLE_NAME = "films";
    private static final String FILM_TABLE_ID_COLUMN_NAME = "film_id";

    private final JdbcTemplate jdbcTemplate;


    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<Film> findAll() {
        List<Film> foundedFilm = jdbcTemplate.query(GET_ALL_FILMS.getTitle(), new FilmMapper());
        return foundedFilm;
    }

    @Override
    public Film save(Film film) {
        filmInsertAndSetId(film);
        addGenresToFilm(film);
        return film;
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

    public Optional<Film> getValidFilmByFilmId(Long filmId) {
        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(GET_FILM_BY_FILM_ID.getTitle(), new FilmMapper(), filmId));
        } catch (DataAccessException e) {
            throw new FilmNotExistException("Фильм не найден" + e.getMessage());
        }
    }

    public boolean isLikeExistsInFilm(long filmId, long userId) {
        return getLikesByFilmId(filmId).contains(userId);
    }

    public void filmInsertAndSetId(Film film) {
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

    public List<Genre> getGenresMatch(List<Genre> genres1, List<Genre> genres2) {
        return genres1.stream()
                .filter(front -> genres2.stream().anyMatch(db -> db.getId() == front.getId()))
                .collect(Collectors.toList());
    }

    public void deleteFilmGenresFromDb(List<Genre> genres) {
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(DELETE_FILM_GENRES_BY_GENRE_ID.getTitle(), genre.getId());
            }
        }
    }

    public void updateFilmGenres(List<Genre> genresFromFrontEnd, Film film) {
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
