package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum GenreSqlOperation {

    GET_ALL_GENRES(
            "SELECT * " +
                    "FROM genre"),
    GET_GENRE_BY_GENRE_ID(
            "SELECT * " +
                    "FROM genre " +
                    "WHERE genre_id = ?"),
    GET_GENRES_BY_FILM_ID(
            "SELECT g.genre_id, g.genre_name " +
                    "FROM film_genre AS f " +
                    "LEFT JOIN genre AS g ON f.genre_id=g.genre_id " +
                    "WHERE film_id = ?"),
    GET_GENRES_ID_BY_FILM_ID(
            "SELECT genre_id " +
                    "FROM film_genre " +
                    "WHERE film_id = ?");

    private final String title;

    GenreSqlOperation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
