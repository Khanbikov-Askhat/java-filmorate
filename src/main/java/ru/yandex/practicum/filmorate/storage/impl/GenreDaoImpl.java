package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmGenreMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.storage.sqloperation.GenreSqlOperation.*;

@Repository
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query(GET_ALL_GENRES.getTitle(), new GenreMapper());
    }

    @Override
    public Optional<Genre> getGenreById(Long genreId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(GET_GENRE_BY_GENRE_ID.getTitle(), new GenreMapper(), genreId));
        } catch (DataAccessException e) {
            throw new GenreNotFoundException("Рейтинг не найден" + e.getMessage());
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        return jdbcTemplate.query(GET_GENRES_BY_FILM_ID.getTitle(), new GenreMapper(), filmId);

    }

    @Override
    public List<Genre> getGenresIdByFilmId(Long filmId) {
        return jdbcTemplate.query(GET_GENRES_ID_BY_FILM_ID.getTitle(), new FilmGenreMapper(), filmId);
    }
}
