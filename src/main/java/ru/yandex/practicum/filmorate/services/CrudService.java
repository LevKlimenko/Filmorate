package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CrudService<T> {
    List<T> getAll();

    T create(T t);

    T update(T t);

    T findById(Long id);
}