package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.Genres;
import ru.yandex.practicum.filmorate.services.genres.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genres> getAll(){
        return genreService.getAll();
    }

    @GetMapping("/{genreId}")
    public Genres getById(@PathVariable("genreId") Long id){
        return genreService.findById(id);
    }
}
