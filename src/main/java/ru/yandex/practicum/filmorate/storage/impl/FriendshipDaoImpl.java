package ru.yandex.practicum.filmorate.storage.impl;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.user.FriendNotAddedException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.sqloperation.UserSqlOperation.*;

@Repository
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (isFriendDontExistsInUser(userId, friendId)) {
            if (isUserExistsInFriend(userId, friendId)) {
                jdbcTemplate.update(ADD_FRIEND.getTitle(), userId, friendId, true);
                updateToTrueStateOfFriendship(friendId, userId);
            } else {
                jdbcTemplate.update(ADD_FRIEND.getTitle(), userId, friendId, false);
            }
        } else {
            throw new FriendNotAddedException("Друг уже добавлен");
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        jdbcTemplate.update(DELETE_FRIEND.getTitle(), userId, friendId);
        if (isUserExistsInFriend(userId, friendId)) {
            updateToFalseStateOfFriendship(friendId, userId);
        }
    }

    @Override
    public List<User> getFriends(long userId) {
        return jdbcTemplate.query(GET_FRIENDS_BY_USER_ID.getTitle(), new UserMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS.getTitle(), new UserMapper(), userId, friendId);
    }

    private boolean isFriendDontExistsInUser(long userId, long friendId) {
        return getFriends(userId).stream()
                .noneMatch(userFriend -> userFriend.getId() == friendId);
    }

    private boolean isUserExistsInFriend(long userId, long friendId) {
        return getFriends(friendId).stream()
                .anyMatch(userFriend -> userFriend.getId() == userId);
    }

    public void updateToTrueStateOfFriendship(long user1, long user2) {
        jdbcTemplate.update(UPDATE_STATE_OF_FRIENDSHIP.getTitle(), true, user1, user2);
    }

    public void updateToFalseStateOfFriendship(long user1, long user2) {
        jdbcTemplate.update(UPDATE_STATE_OF_FRIENDSHIP.getTitle(), true, user1, user2);
    }
}
