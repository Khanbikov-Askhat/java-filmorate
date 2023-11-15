# java-filmorate
Template repository for Filmorate project.
# DB structure
![DB_stucture](DB structure/DB_structure.png)

# Request examples
- Get film with id 5
  ```` SQL
  SELECT *
  FROM film
  WHERE film_id = 5;

- Get user with id 2
  ```` SQL
  SELECT *
  FROM user
  WHERE user_id = 2;

- Get 10 most popular films
  ```` SQL
  SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration,r.mpa_id, r.mpa_name
  FROM films AS f
  JOIN rating AS r ON f.mpa_id = r.mpa_id
  LEFT JOIN FILM_LIKE AS l ON f.film_id = l.film_id
  GROUP BY f.film_id
  ORDER BY COUNT(l.user_id) DESC
  LIMIT 10;

- Get friends of user with id 3
  ```` SQL
  SELECT *
  FROM friendship
  WHERE user_id = 3 
  AND state_of_friendship = true;

- Get all users
  ```` SQL
  SELECT *
  FROM user;

- Get all films
  ```` SQL
  SELECT *
  FROM film;

- Get common friends of two users
  ```` SQL
  SELECT * FROM users AS us
  JOIN FRIENDSHIP AS fr1 ON us.user_id = fr1.friend_id
  JOIN FRIENDSHIP AS fr2 ON us.user_id = fr2.friend_id
  WHERE fr1.user_id = ? AND fr2.user_id = ?;