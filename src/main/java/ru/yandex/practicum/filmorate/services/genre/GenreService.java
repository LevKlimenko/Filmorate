package ru.yandex.practicum.filmorate.services.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

@Service
public class GenreService implements GenreServiceInterface {
    public final ru.yandex.practicum.filmorate.storages.genre.GenreStorageInterface genreStorage;

    @Autowired
    public GenreService(ru.yandex.practicum.filmorate.storages.genre.GenreStorageInterface genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre findById(Long id) {
        return genreStorage.findGenreById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreStorage.findAllGenre();
    }
}