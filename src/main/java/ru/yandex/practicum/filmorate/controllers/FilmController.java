package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private Integer filmId = 0;
    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            throw new ValidationException("Film already exists");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film should be released after 27.12.1895");
        } else {
            int id = ++filmId;
            film.setId(id);
            films.put(id, film);
            log.debug(film.toString());
            return film;
        }
    }

    @PutMapping(value = "/films")
    public Film AddOrUpdate(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film must be released after 27.12.1895");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug(film.toString());
            return film;
        } else {
            throw new FilmNotExistException("List don't contains this film");
        }
    }
}
