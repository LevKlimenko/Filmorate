package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.services.mpa.MpaServiceInterface;

import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaServiceInterface mpaService;

    @GetMapping
    public Collection<Mpa> getAll() {
        return mpaService.getAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa getById(@PathVariable("mpaId") Long id) {
        return mpaService.findById(id);
    }
}