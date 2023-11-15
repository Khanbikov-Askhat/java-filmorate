package ru.yandex.practicum.filmorate.service.implservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.rating.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.dao.RatingDao;

import java.util.List;
import java.util.Optional;

@Service
public class MpaServiceImpl {
    private final RatingDao ratingDao;

    @Autowired
    public MpaServiceImpl(@Qualifier("ratingDaoImpl") RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public Optional<Rating> findById(Long ratingId) {
        if (ratingDao.getRatingById(ratingId).isEmpty()) {
            throw new RatingNotFoundException("Пользователь с id: " + ratingId + " не найден");
        }
        if (ratingId < 0) {
            throw new RatingNotFoundException("Рейтинг не найден");
        }
        return ratingDao.getRatingById(ratingId);
    }

    public List<Rating> findAll() {
        return ratingDao.getAllRatings();
    }
}