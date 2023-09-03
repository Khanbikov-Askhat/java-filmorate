package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public HashMap<Integer, Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            throw new ValidationException("Film already exists");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film should be released after 27.12.1895");
        } else {
            films.put(film.getId(), film);
            log.debug(film.toString());
            return film;
        }
    }

    @PutMapping(value = "/films")
    public Film AddOrUpdate(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film must be released after 27.12.1895");
        }
        if (films.containsValue(film)) {
            for (Integer id: films.keySet()) {
                if (films.get(id).equals(film)) {
                    log.debug(film.toString());
                    films.put(id, film);
                }
            }
        } else {
            log.debug(film.toString());
            films.put(film.getId(), film);
        }
        return film;
    }
}
