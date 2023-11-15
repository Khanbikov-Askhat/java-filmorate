package ru.yandex.practicum.filmorate.controllers.implcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.implservice.FilmServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmImplController {

    private final FilmServiceImpl filmService;

    public FilmImplController(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Пришел запрос GET /films");
        List<Film> foundedFilms = filmService.findAll();
        log.debug("Отправлен овтет на GET запрос /films с телом: {}", foundedFilms);
        return foundedFilms;
    }

    @GetMapping("/{filmId}")
    public Optional<Film> findById(@PathVariable("filmId") @Min(0) Long filmId) {
        log.info("Получен GET-запрос /films/{}", filmId);
        Optional<Film> foundedFilm = filmService.findById(filmId);
        log.info("Отправлен ответ на GET-запрос /films/{} с телом: {}", filmId, foundedFilm);
        return foundedFilm;

    }

    @PostMapping
    public Optional<Film> save(@RequestBody @Valid Film film) {
        log.info("Пришел POST-запрос /films с телом: {}", film);
        Optional<Film> createdFilm = filmService.save(film);
        log.info("Отправлен ответ на POST-запрос /films с телом: {}", createdFilm);
        return createdFilm;
        //return filmService.save(film);
    }

    @PutMapping
    public Optional<Film> update(@RequestBody @Valid Film film) {
        log.info("Пришел PUT-запрос /films с телом: {}", film);
        Optional<Film> updatedFilm = filmService.update(film);
        log.info("Отправлен ответ на PUT-запрос /films с телом: {}", updatedFilm);
        return updatedFilm;
        //return filmService.update(film);
    }

    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable("filmId") @Min(0) Long filmId) {
        log.info("Получен DELETE-запрос /films/{}", filmId);
        log.info("Отправлен ответ на PUT-запрос /films/{}", filmId);
        filmService.delete(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") @Min(0) Long filmId,
                        @PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен PUT-запрос /films/{}/like/{}", filmId, userId);
        log.info("Отправлен ответ на PUT-запрос /films/{}/like/{}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") @Min(0) Long filmId,
                           @PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен DELETE-запрос /films/{}/like/{}", filmId, userId);
        log.info("Отправлен ответ на DELETE-запрос /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
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
