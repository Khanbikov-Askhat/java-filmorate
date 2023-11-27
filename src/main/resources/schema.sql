CREATE TABLE IF NOT EXISTS rating
(
    mpa_id BIGSERIAL PRIMARY KEY,
    mpa_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    film_id BIGSERIAL PRIMARY KEY,
    film_name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    duration INTEGER,
    release_date DATE,
    mpa_id INTEGER,
    FOREIGN KEY(mpa_id) REFERENCES rating
);

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    login VARCHAR(100),
    email VARCHAR(100),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    state_of_friendship BOOLEAN NOT NULL,
    PRIMARY KEY(user_id, friend_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY(friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY(film_id, user_id),
    FOREIGN KEY(film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id BIGSERIAL PRIMARY KEY,
    genre_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY(film_id, genre_id),
    FOREIGN KEY(film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY(genre_id) REFERENCES genre(genre_id) ON DELETE CASCADE
);