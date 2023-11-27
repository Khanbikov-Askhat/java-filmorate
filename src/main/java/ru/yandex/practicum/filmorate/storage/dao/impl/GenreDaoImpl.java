package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmGenreMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.classdao.GenreDao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import static java.util.function.UnaryOperator.identity;
import static ru.yandex.practicum.filmorate.storage.dao.sqloperation.GenreSqlOperation.*;

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
}
