package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum RatingSqlOperation {

    GET_ALL_RATINGS(
            "SELECT * " +
                    "FROM rating"),
    GET_RATING_BY_RATING_ID(
            "SELECT * " +
                    "FROM rating " +
                    "WHERE mpa_id = ?");

    private final String title;

    RatingSqlOperation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}