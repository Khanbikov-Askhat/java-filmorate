package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.film.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long generatorId = 0L;

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        validateFilm(film);
        Long id = ++generatorId;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    public Film update(Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException("В списке нет такого фильма");
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilmById(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new FilmNotExistException("В списке нет такого фильма");
        }
        return film;
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film must be released after 27.12.1895");
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Film must have a name");
        }
    }
}
