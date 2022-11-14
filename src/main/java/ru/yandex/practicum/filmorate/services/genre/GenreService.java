package ru.yandex.practicum.filmorate.services.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    public final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre findById(Long id) {
        return genreDbStorage.findGenreById(id);
    }

    public List<Genre> getAll() {
        return genreDbStorage.findAllGenre();
    }

    public List<Genre> findGenresOfFilm(Long id) {
        return genreDbStorage.findGenresOfFilm(id);
    }
}