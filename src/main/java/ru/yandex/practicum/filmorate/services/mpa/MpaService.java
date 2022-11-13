package ru.yandex.practicum.filmorate.services.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storages.mpa.MpaDbStorage;

import java.util.Collection;
import java.util.Map;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<Mpa> getAll() {
        return mpaDbStorage.findAllMpa();
    }

    public Mpa findById(Long id) {
        return mpaDbStorage.findMpaById(id);
    }

}
