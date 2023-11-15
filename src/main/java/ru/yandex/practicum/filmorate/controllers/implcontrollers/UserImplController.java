package ru.yandex.practicum.filmorate.controllers.implcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.implservice.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserImplController {

    private final UserServiceImpl userService;

    public UserImplController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> findAll() {
        log.info("Получен GET-запрос /users");
        List<User> foundedUsers = userService.findAll();
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", foundedUsers);
        return foundedUsers;
    }

    @GetMapping("/{userId}")
    public Optional<User> findById(@PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен GET-запрос /users/{}", userId);
        Optional<User> foundedUser = userService.findById(userId);
        log.info("Отправлен ответ на GET-запрос /users/{} с телом: {}", userId, foundedUser);
        return foundedUser;
    }


    @PostMapping
    public Optional<User> save(@RequestBody @Valid User user) {
        log.info("Получен POST-запрос /users с телом: {}", user);
        Optional<User> createdUser = userService.save(user);
        log.info("Отправлен ответ на POST-запрос /users с телом: {}", createdUser);
        return createdUser;
    }


    @PutMapping
    public Optional<User> update(@RequestBody @Valid User user) {
        log.info("Получен PUT-запрос /users с телом: {}", user);
        Optional<User> updatedUser = userService.update(user);
        log.info("Отправлен ответ на PUT-запрос /users с телом: {}", updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") @Min(0) Long userId) {
        log.info("Получен DELETE-запрос /users/{}", userId);
        log.info("Отправлен ответ на DELETE-запрос /users/{}", userId);
        userService.delete(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") @Min(0) Long userId,
                          @PathVariable("friendId") @Min(0) Long friendId) {
        log.info("Получен PUT-запрос /users/{}/friends/{}", userId, friendId);
        log.info("Отправлен ответ на PUT-запрос /users/{}/friends/{}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") @Min(0) Long userId,
                             @PathVariable("friendId") @Min(0) Long friendId) {
        log.info("Получен DELETE-запрос /users/{}/friends/{}", userId, friendId);
        log.info("Отправлен ответ на DELETE-запрос /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") @Min(0) Long userId) {
        log.info("Получен GET-запрос /users/{id}/friends");
        List<User> friendList = userService.getFriends(userId);
        log.info("Отправлен ответ на GET-запрос /users с телом: {}", friendList);
        return friendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") @Min(0) Long userId,
                                       @PathVariable("otherId") @Min(0) Long friendId) {
        log.info("Получен GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {}", userId, friendId);
        List<User> foundedCommonFriends = userService.getCommonFriends(userId, friendId);
        log.info("Отправлен ответ на GET-запрос users/{id}/friends/common/{otherId} с id {} " +
                "и otherId {} c телом {}", userId, friendId, foundedCommonFriends);
        return foundedCommonFriends;
    }
}
