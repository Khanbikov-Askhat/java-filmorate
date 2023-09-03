package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private Integer userId = 0;
    private HashMap<Integer,User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Текущее количество фильмов: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        if ((user.getName() == null)) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().contains(" ")) {
            log.debug("User's email must not have whitespaces", user.getEmail());
            throw new ValidationException("User's email must not have whitespaces");
        }
        if (user.getLogin().contains(" ")) {
            log.debug("User's login must not have whitespaces", user.getEmail());
            throw new ValidationException("User's login must not have whitespaces");
        }
        if (users.containsValue(user)) {
            log.debug("User's email must not have whitespaces", user);
            throw new ValidationException("User already exists");
        }
        int id = ++userId;
        user.setId(id);
        users.put(id, user);
        log.debug(user.toString());
        return user;

    }

    @PutMapping(value = "/users")
    public User AddOrUpdate(@Valid @RequestBody User user) {
        if (user.getEmail().contains(" ")) {
            log.debug("User's email must not have whitespaces", user.getEmail());
            throw new ValidationException("User's email must not have whitespaces");
        }
        if (user.getLogin().contains(" ")) {
            log.debug("User's login must not have whitespaces", user.getEmail());
            throw new ValidationException("User's login must not have whitespaces");
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug(user.toString());
            return user;
        } else {
            throw new UserNotExistException("This film does not exist");
        }
    }

}
