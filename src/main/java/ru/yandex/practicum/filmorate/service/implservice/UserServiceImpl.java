package ru.yandex.practicum.filmorate.service.implservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl {

    private UserDao userDao;
    private FriendshipDao friendshipDao;

    public List<User> findAll() {
        return userDao.findAll();
    }


    public Optional<User> findById(Long userId) {
        return userDao.getUserById(userId);
    }

    public Optional<User> save(User user) {
        validateUser(user);
        return userDao.save(user);
    }

    public Optional<User> update(User user) {
        userValidation(user);
        return userDao.update(user);
    }

    public void delete(Long userId) {
        userIdExistsValidation(userId);
        userDao.delete(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        userIdExistsValidation(userId);
        userIdExistsValidation(friendId);
        friendshipDao.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userIdExistsValidation(userId);
        userIdExistsValidation(friendId);
        friendshipDao.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        userIdExistsValidation(userId);
        return friendshipDao.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        userIdExistsValidation(userId);
        userIdExistsValidation(friendId);
        return friendshipDao.getCommonFriends(userId, friendId);
    }


    private void userValidation(User user) {
        if (user.getId() < 0 || userDao.getUserById(user.getId()).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id: " + user.getId() + " не найден");
        }
        if (user.getBirthday() == null) {
            throw new ValidationException("Отсутствует дата рождения");
        }
    }

    private void userIdExistsValidation(Long userId) {
        if (userDao.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id: " + userId + " не найден");
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

    @Autowired
    @Qualifier("userDaoImpl")
    public void setUserDao(UserDao userStorage) {
        this.userDao = userStorage;
    }

    @Autowired
    @Qualifier("friendshipDaoImpl")
    public void setFriendshipDao(FriendshipDao friendshipDao) {
        this.friendshipDao = friendshipDao;
    }
}