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
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private Long generatorId = 0L;
    private Map<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Пришел GET-запрос /users");
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", new ArrayList<>(users.values()));
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Пришел POST-запрос /users с телом: {}", user);
        validateUser(user);
        if (users.containsValue(user)) {
            log.debug("User's email must not have whitespaces", user);
            throw new ValidationException("User already exists");
        }
        Long id = ++generatorId;
        user.setId(id);
        users.put(id, user);
        log.debug(user.toString());
        log.info("Отправлен ответ на POST-запрос /users с телом: {}", user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Пришел PUT-запрос /users с телом: {}", user);
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new UserNotExistException("This user does not exist");
        }
        users.put(user.getId(), user);
        log.debug(user.toString());
        log.info("Отправлен ответ на PUT-запрос /users с телом: {}", user);
        return user;
    }

    public void validateUser(User user) {
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
    }
}
