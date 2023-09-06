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
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private Long generatorId = 0L;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел POST-запрос /films с телом: {}", film);
        validateFilm(film);
        Long id = ++generatorId;
        film.setId(id);
        films.put(id, film);
        log.info("Отправлен ответ на POST-запрос /films с телом: {}", film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел PUT-запрос /films с телом: {}", film);
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException("List don't contains this film");
        }
        films.put(film.getId(), film);
        log.info("Отправлен ответ на PUT-запрос /films с телом: {}", film);
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
