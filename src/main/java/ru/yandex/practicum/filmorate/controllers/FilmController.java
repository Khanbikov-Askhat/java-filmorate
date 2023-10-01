package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Пришел запрос GET /films");
        List<Film> foundedFilms = filmService.findAll();
        log.debug("Отправлен овтет на GET запрос /films с телом: {}", foundedFilms);
        return foundedFilms;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Пришел POST-запрос /films с телом: {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Отправлен ответ на POST-запрос /films с телом: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Пришел PUT-запрос /films с телом: {}", film);
        Film updatedFilm = filmService.update(film);
        log.info("Отправлен ответ на PUT-запрос /films с телом: {}", updatedFilm);
        return updatedFilm;
    }

    @GetMapping("/{id}")
    public Film getUserById(@PathVariable @Min(1) Long id) {
        log.info("Получен GET-запрос /films/{}", id);
        Film foundedFilm = filmService.getFilmById(id);
        log.info("Отправлен ответ на GET-запрос /films/{} с телом: {}", id, foundedFilm);
        return foundedFilm;

    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable @Min(1) Long id,
                          @PathVariable @Min(1) Long userId) {
        log.info("Получен PUT-запрос /films/{}/like/{}", id, userId);
        Film addedLikeFilm = filmService.addLike(id, userId);
        log.info("Отправлен ответ на PUT-запрос /films/{}/like/{} c телом: {}", id, userId, addedLikeFilm);
        return addedLikeFilm;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike (@PathVariable @Min(1) Long id,
                            @PathVariable @Min(1) Long userId) {
        log.info("Получен DELETE-запрос /films/{}/like/{}", id, userId);
        Film deletedLikeFromFilm = filmService.deleteLike(id, userId);
        log.info("Отправлен ответ на DELETE-запрос /films/{}/like/{} c телом: {}", id, userId, deletedLikeFromFilm);
        return deletedLikeFromFilm;
    }

    @GetMapping("/popular")
    public List<Film> getSortedFilmsByLikes(@RequestParam(value = "count", required = false, defaultValue = "10")
                                                @Min(1) Long count) {
        log.info("Получен GET-запрос /popular?count={}", count);
        List<Film> filmsList = filmService.getSortedFilmsByLikes(count);
        log.info("Отправлен ответ на GET-запрос /popular c телом {}", filmsList);
        return filmsList;
    }
}
