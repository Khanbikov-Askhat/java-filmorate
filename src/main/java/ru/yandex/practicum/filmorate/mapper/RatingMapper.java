package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingMapper implements RowMapper<Rating> {

    @Override
    public Rating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Rating.builder()
                .id(resultSet.getLong("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
