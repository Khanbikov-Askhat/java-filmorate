package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum UserSqlOperation {

    GET_ALL_USERS(
            "SELECT * " +
                    "FROM users"),
    GET_USER_BY_USER_ID(
            "SELECT * " +
                    "FROM users " +
                    "WHERE user_id = ?"),
    UPDATE_USER(
            "UPDATE users " +
                    "SET user_name = ?, login = ?, email = ?, birthday = ? " +
                    "WHERE user_id = ?"),
    DELETE_USER(
            "DELETE FROM users " +
                    "WHERE user_id = ?"),
    DELETE_FILM_LIKE_BY_USER_ID(
            "DELETE FROM film_like " +
                    "WHERE user_id = ?"),
    DELETE_FRIENDSHIP_BY_USER_ID(
            "DELETE FROM friendship " +
                    "WHERE user_id AND friend_id IN(?)"),
    ADD_FRIEND(
            "INSERT INTO friendship(user_id, friend_id, state_of_friendship) " +
                    "VALUES(?, ?, ?)"),
    DELETE_FRIEND(
            "DELETE FROM friendship " +
                    "WHERE user_id = ? AND friend_id = ?"),
    GET_FRIENDS_BY_USER_ID(
            "SELECT * " +
                    "FROM users " +
                    "WHERE user_id IN (SELECT friend_id FROM friendship WHERE user_id = ?)"),
    UPDATE_STATE_OF_FRIENDSHIP(
            "UPDATE friendship " +
                    "SET state_of_friendship = ? " +
                    "WHERE user_id = ? AND friend_id = ?"),
    GET_COMMON_FRIENDS(
            "SELECT * FROM users AS us " +
                    "JOIN FRIENDSHIP AS fr1 ON us.user_id = fr1.friend_id " +
                    "JOIN FRIENDSHIP AS fr2 ON us.user_id = fr2.friend_id " +
                    "WHERE fr1.user_id = ? AND fr2.user_id = ?");

    private final String title;

    UserSqlOperation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
