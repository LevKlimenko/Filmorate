package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface CrudService<T> {
    Collection<T> getAll();

    T create(T t);

    T update(T t);

    T findById(Long id);
}
