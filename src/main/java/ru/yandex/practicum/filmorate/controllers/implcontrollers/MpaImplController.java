package ru.yandex.practicum.filmorate.controllers.implcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.implservice.MpaServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaImplController {
    private final MpaServiceImpl mpaService;

    @Autowired
    public MpaImplController(MpaServiceImpl mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Rating> findAll() {
        log.debug("Пришел запрос GET /mpa");
        List<Rating> foundedMpa = mpaService.findAll();
        log.debug("Отправлен овтет на GET запрос /mpa с телом: {}", foundedMpa);
        return foundedMpa;
    }

    @GetMapping("/{mpaId}")
    public Optional<Rating> findById(@PathVariable Long mpaId) {
        log.info("Пришел GET-запрос /mpa/{}", mpaId);
        Optional<Rating> mpa = mpaService.findById(mpaId);
        log.info("Отправлен ответ на GET-запрос /mpa/{} c телом: {}", mpaId, mpa);
        return mpa;
    }
}