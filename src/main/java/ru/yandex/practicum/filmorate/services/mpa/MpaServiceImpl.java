package ru.yandex.practicum.filmorate.services.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storages.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa findById(Long id) {
        return mpaStorage.findById(id);
    }
}