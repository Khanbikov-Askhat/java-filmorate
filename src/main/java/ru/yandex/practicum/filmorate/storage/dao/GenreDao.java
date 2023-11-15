package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    List<Genre> getGenres();

    Optional<Genre> getGenreById(Long genreId);

    List<Genre> getGenresByFilmId(Long filmId);

    List<Genre> getGenresIdByFilmId(Long filmId);
}
