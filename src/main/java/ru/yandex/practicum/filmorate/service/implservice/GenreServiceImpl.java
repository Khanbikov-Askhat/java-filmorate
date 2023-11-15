package ru.yandex.practicum.filmorate.service.implservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;
import java.util.Optional;


@Service
public class GenreServiceImpl {
    private final GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(@Qualifier("genreDaoImpl") GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Optional<Genre> findById(Long genreId) {
        if (!genreDao.getGenreById(genreId).isPresent()) {
            throw new GenreNotFoundException("Жанр с id: " + genreId + " не найден");
        }
        if (genreId < 0) {
            throw new GenreNotFoundException("Не верное значение id");
        }
        return genreDao.getGenreById(genreId);
    }

    public List<Genre> findAll() {
        return genreDao.getGenres();
    }
}
