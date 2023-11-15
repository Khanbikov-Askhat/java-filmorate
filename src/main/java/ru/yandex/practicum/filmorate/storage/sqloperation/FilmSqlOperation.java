package ru.yandex.practicum.filmorate.storage.sqloperation;

public enum FilmSqlOperation {

    CREATE_FILM_GENRE(
            "INSERT INTO film_genre(film_id, genre_id) " +
                    "VALUES(?, ?)"),
    GET_ALL_FILMS(
            "SELECT f.film_id, f.film_name, f.description, f.duration, " +
                    "f.release_date, r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id"),
    GET_FILM_BY_FILM_ID(
            "SELECT f.film_id, f.film_name, f.description, f.duration," +
                    "f.release_date, r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id " +
                    "WHERE film_id = ?"),
    UPDATE_FILM(
            "UPDATE films " +
                    "SET film_name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                    "WHERE film_id = ?"),
    DELETE_FILM(
            "DELETE FROM films " +
                    "WHERE film_id = ?"),
    DELETE_FILM_GENRES_BY_FILM_ID(
            "DELETE FROM film_genre " +
                    "WHERE film_id = ?"),
    DELETE_FILM_GENRES_BY_GENRE_ID(
            "DELETE FROM film_genre " +
                    "WHERE genre_id = ?"),
    GET_USER_LIKES_BY_FILM_ID(
            "SELECT user_id " +
                    "FROM film_like " +
                    "WHERE film_id = ?"),
    CREATE_LIKE(
            "INSERT INTO film_like(film_id, user_id) " +
                    "VALUES(?, ?)"),
    DELETE_LIKE(
            "DELETE FROM film_like " +
                    "WHERE film_id = ? AND user_id = ?"),
    GET_MOST_POPULAR_FILMS(
            "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration,r.mpa_id, r.mpa_name " +
                    "FROM films AS f " +
                    "JOIN rating AS r ON f.mpa_id = r.mpa_id " +
                    "LEFT JOIN FILM_LIKE AS l ON f.film_id = l.film_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(l.user_id) DESC " +
                    "LIMIT ?");

    private final String title;

    FilmSqlOperation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
