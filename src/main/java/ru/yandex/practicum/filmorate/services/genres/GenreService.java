package ru.yandex.practicum.filmorate.services.genres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Genres;
import ru.yandex.practicum.filmorate.storages.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    public final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genres findById(Long id) {
        return genreDbStorage.findGenreById(id);
    }

    public List<Genres> getAll() {
        return genreDbStorage.findAllGenre();
    }

    public List<Genres> findGenresOfFilm(Long id) {
        return genreDbStorage.findGenresOfFilm(id);
    }
}
