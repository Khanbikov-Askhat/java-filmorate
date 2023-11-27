package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.implservice.GenreServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreImplController {
    private final GenreServiceImpl genreService;

    @Autowired
    public GenreImplController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> findAll() {
        log.debug("Пришел запрос GET /genres");
        List<Genre> foundedGenres = genreService.findAll();
        log.debug("Отправлен овтет на GET запрос /genres с телом: {}", foundedGenres);
        return foundedGenres;
    }

    @GetMapping("/{genreId}")
    public Genre findById(@PathVariable Long genreId) {
        log.info("Пришел GET-запрос /genres/{}", genreId);
        Genre genre = genreService.findById(genreId);
        log.info("Отправлен ответ на GET-запрос /genres/{} с телом: {}", genreId, genre);
        return genre;
    }
}
