package ru.yandex.practicum.filmorate.services.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.Collection;

@Service
public class MpaStorageInterface implements MpaServiceInterface{
    private final ru.yandex.practicum.filmorate.storages.mpa.MpaStorageInterface mpaStorage;

    @Autowired
    public MpaStorageInterface(ru.yandex.practicum.filmorate.storages.mpa.MpaStorageInterface mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Collection<Mpa> getAll() {
        return mpaStorage.findAllMpa();
    }

    @Override
    public Mpa findById(Long id) {
        return mpaStorage.findMpaById(id);
    }
}