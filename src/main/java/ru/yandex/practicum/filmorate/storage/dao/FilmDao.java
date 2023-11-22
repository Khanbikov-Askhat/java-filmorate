package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    List<Film> findAll();

    Film save(Film film);

    Optional<Film> update(Film film);

    void delete(Long id);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getSortedFilmsByLikes(Long count);

    List<Long> getLikesByFilmId(long filmId);

    Optional<Film> getValidFilmByFilmId(Long filmId);

    void updateFilmGenres(List<Genre> genresFromUI, Film film);

    void deleteFilmGenresFromDb(List<Genre> genresFromDbByFilm);

    List<Genre> getGenresMatch(List<Genre> genresFromUI, List<Genre> genresFromDbByFilm);
}
