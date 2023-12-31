package ru.yandex.practicum.filmorate.storage.dao.classdao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {

    List<Mpa> getAllRatings();

    Optional<Mpa> getRatingById(Long ratingId);
}