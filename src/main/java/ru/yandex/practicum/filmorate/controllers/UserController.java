package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Получен GET-запрос /users");
        List<User> foundedUsers = userService.findAll();
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", foundedUsers);
        return foundedUsers;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос /users с телом: {}", user);
        User createdUser = userService.create(user);
        log.info("Отправлен ответ на POST-запрос /users с телом: {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос /users с телом: {}", user);
        User updatedUser = userService.update(user);
        log.info("Отправлен ответ на PUT-запрос /users с телом: {}", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable @Min(1) Long id) {
        log.info("Получен GET-запрос /users/{}", id);
        User foundedUser = userService.getUserById(id);
        log.info("Отправлен ответ на GET-запрос /users/{} с телом: {}", id, foundedUser);
        return foundedUser;

    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable @Min(1) Long id,
                          @PathVariable @Min(1) Long friendId) {
        log.info("Получен PUT-запрос /users/{}/friends/{}", id, friendId);
        User addedToUser = userService.addFriend(id, friendId);
        log.info("Отправлен ответ на PUT-запрос /users/{}/friends/{} c телом: {}", id, friendId, addedToUser);
        return addedToUser;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable @Min(1) Long id,
                             @PathVariable @Min(1) Long friendId) {
        log.info("Получен DELETE-запрос /users/{}/friends/{}", id, friendId);
        User deletedFromUser = userService.deleteFriend(id, friendId);
        log.info("Отправлен ответ на DELETE-запрос /users/{}/friends/{} c телом: {}", id, friendId, deletedFromUser);
        return deletedFromUser;
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable @Min(1) Long id) {
        log.info("Получен GET-запрос /users/{id}/friends");
        List<User> friendList = userService.getAllFriends(id);
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", friendList);
        return friendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @Min(1) long id,
                                       @PathVariable @Min(1) long otherId) {
        log.info("Получен GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {}", id, otherId);
        List<User> foundedCommonFriends = userService.getCommonFriends(id, otherId);
        log.info("Отправлен ответ на GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {} c телом {}", id, otherId, foundedCommonFriends);
        return foundedCommonFriends;
    }

}
