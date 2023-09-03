package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@RestController
@Slf4j
public class UserController {

    private HashMap<Integer,User> users = new HashMap<>();

    @GetMapping("/users")
    public HashMap<Integer, User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        if (user.getEmail().contains(" ")) {
            log.debug("User's email must not have whitespaces", user.getEmail());
            throw new ValidationException("User's email must not have whitespaces");
        } else if (users.containsValue(user)) {
            log.debug("User's email must not have whitespaces", user);
            throw new ValidationException("User already exists");
        } else if (user.getName().isEmpty() || (user.getName() == null)) {
            user.setName(user.getLogin());
            users.put(user.getId(), user);
            log.debug(user.toString());
            return user;
        } else {
            users.put(user.getId(), user);
            log.debug(user.toString());
            return user;
        }
    }

    @PutMapping(value = "/users")
    public User AddOrUpdate(@RequestBody User user) {
        if (user.getEmail().contains(" ")) {
            log.debug("User's email must not have whitespaces", user.getEmail());
            throw new ValidationException("User's email must not have whitespaces");
        }
        if (users.containsValue(user)) {
            for (Integer id: users.keySet()) {
                if (users.get(id).equals(user)) {
                    users.put(id, user);
                }
            }
        } else {
            users.put(user.getId(), user);
        }
        return user;
    }
}
