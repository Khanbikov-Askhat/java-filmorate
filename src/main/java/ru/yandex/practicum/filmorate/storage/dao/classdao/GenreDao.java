package ru.yandex.practicum.filmorate.storage.dao.classdao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    List<Genre> getGenres();

    Optional<Genre> getGenreById(Long genreId);

    List<Genre> getGenresByFilmId(Long filmId);

    List<Genre> getGenresIdByFilmId(Long filmId);

    void load(List<Film> films);
}
