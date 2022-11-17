package ru.yandex.practicum.filmorate.services.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storages.genre.GenreStorage;

import java.util.List;

@Service
public class GenreServiceImp implements GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceImp(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Genre findById(Long id) {
        return genreStorage.findById(id);
    }

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }
}