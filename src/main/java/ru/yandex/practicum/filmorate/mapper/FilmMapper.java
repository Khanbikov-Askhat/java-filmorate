package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getLong("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .mpa(new Mpa(resultSet.getInt("mpa_id"),
                        resultSet.getString("mpa_name"))).build();
    }
}
