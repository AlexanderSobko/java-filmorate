package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService implements EntityService<Genre> {

    private final GenreStorage storage;

    @Override
    public Genre save(Genre entity) {
        return null;
    }

    @Override
    public Genre get(Integer id) {
        validateNotExists(id);
        return storage.findById(id);
    }

    @Override
    public Genre update(Genre entity) {
        return null;
    }

    @Override
    public List<Genre> getAll() {
        return storage.findAll();
    }

    private void validateNotExists(int id) {
        if (!storage.exists(id)) {
            throw new NotExistsException("Жанр", id);
        }
    }
}
