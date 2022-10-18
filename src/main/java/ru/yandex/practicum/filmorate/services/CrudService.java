package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface CrudService<T> {
    Collection<T> getAll();

    T create(T t);

    T update(T t);

    T findById(Long id);

    Map<Long, T> getMap();
}
