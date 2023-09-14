package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage {

    private Long generatorId = 0L;
    private Map<Long, User> users = new HashMap<>();


    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(@Valid User user) {
        validateUser(user);
        if (users.containsValue(user)) {
            throw new ValidationException("Пользователь уже существует");
        }
        Long id = ++generatorId;
        user.setId(id);
        users.put(id, user);
        log.debug(user.toString());
        return user;
    }

    public User update(@Valid User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new UserNotExistException("Такого пользователя не существует");
        }
        users.put(user.getId(), user);
        log.debug(user.toString());
        return user;
    }

    public User getUserById(Long id) {
        if (isUserExist(id)) {
            User foundedUser = users.get(id);
            return foundedUser;
        } else {
            throw new UserNotExistException("Такого пользователя не существует");
        }
    }

    public void validateUser(User user) {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().contains(" ")) {
            log.debug("Email пользователя не должен содежать пробелы", user.getEmail());
            throw new ValidationException("Email пользователя не должен содежать пробелы");
        }
        if (user.getLogin().contains(" ")) {
            log.debug("Логин не должен содежать пробелы", user.getEmail());
            throw new ValidationException("Логин не должен содежать пробелы");
        }
    }

    public boolean isUserExist(Long userId) {
        if (users.containsKey(userId)) {
            return true;
        } else {
            return false;
        }
    }
}
