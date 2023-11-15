package ru.yandex.practicum.filmorate.service.inmemoryservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.user.FriendNotAddedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    public User getUserById(Long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = inMemoryUserStorage.getUserById(userId);
        User friend = inMemoryUserStorage.getUserById(friendId);
        if (!user.setFriendsId(friendId)) {
            throw new FriendNotAddedException("Друг с id " + friendId + " не был добавлен");
        }
        if (friend.setFriendsId(userId)) {
            return user;
        } else {
            throw new FriendNotAddedException("Друг с id " + friendId + " не был добавлен");
        }
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = inMemoryUserStorage.getUserById(userId);
        User friend = inMemoryUserStorage.getUserById(friendId);
        user.removeFriend(friendId);
        return user;
    }

    public List<User> getAllFriends(Long userId) {
        User user = inMemoryUserStorage.getUserById(userId);
        List<User> friendList = new ArrayList<User>();
        for (Long friendId : user.getFriendsId()) {
            friendList.add(inMemoryUserStorage.getUserById(friendId));
        }
        return friendList;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        Set<Long> userFriendSet = getUserById(userId).getFriendsId();
        Set<Long> anotherUserFriendSet = getUserById(friendId).getFriendsId();
        if (userFriendSet.isEmpty() || anotherUserFriendSet.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> userCommonFriendsIds = userFriendSet.stream()
                .filter(anotherUserFriendSet::contains)
                .collect(Collectors.toList());
        List<User> userFriends = new ArrayList<>();
        for (Long id : userCommonFriendsIds) {
            userFriends.add(inMemoryUserStorage.getUserById(id));
        }
        return userFriends;
    }


}
