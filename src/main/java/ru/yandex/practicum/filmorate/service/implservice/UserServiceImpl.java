package ru.yandex.practicum.filmorate.service.implservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.exceptions.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.classdao.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.dao.classdao.UserDao;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl {

    private UserDao userDao;
    private FriendshipDao friendshipDao;

    public UserServiceImpl(@Qualifier("userDaoImpl") UserDao userDao, @Qualifier("friendshipDaoImpl") FriendshipDao friendshipDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }


    public User findById(Long userId) {
        return userDao.getUserById(userId).get();
    }

    public User save(User user) {
        validateUser(user);
        return userDao.save(user).get();
    }

    public User update(User user) {
        userValidation(user);
        return userDao.update(user).get();
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

}