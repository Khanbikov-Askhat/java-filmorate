package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAll();

    Optional<User> save(User user);

    Optional<User> update(User user);

    void delete(Long id);

    Optional<User> getUserById(Long id);
}