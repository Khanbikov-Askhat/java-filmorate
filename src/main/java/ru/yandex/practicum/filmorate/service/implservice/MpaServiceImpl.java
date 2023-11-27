package ru.yandex.practicum.filmorate.service.implservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.classdao.MpaDao;

import java.util.List;

@Service
public class MpaServiceImpl {
    private final MpaDao ratingDao;

    @Autowired
    public MpaServiceImpl(@Qualifier("mpaDaoImpl") MpaDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public Mpa findById(Long ratingId) {
        if (ratingDao.getRatingById(ratingId).isEmpty()) {
            throw new MpaNotFoundException("Пользователь с id: " + ratingId + " не найден");
        }
        if (ratingId < 0) {
            throw new MpaNotFoundException("Рейтинг не найден");
        }
        return ratingDao.getRatingById(ratingId).get();
    }

    public List<Mpa> findAll() {
        return ratingDao.getAllRatings();
    }
}