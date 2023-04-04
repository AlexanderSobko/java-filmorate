package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService implements EntityService<Mpa> {

    private final MpaStorage storage;

    @Override
    public Mpa save(Mpa entity) {
        return null;
    }

    @Override
    public Mpa get(Integer id) {
        validateNotExists(id);
        return storage.findById(id);
    }

    @Override
    public Mpa update(Mpa entity) {
        return null;
    }

    @Override
    public List<Mpa> getAll() {
        return storage.findAll();
    }

    private void validateNotExists(int id) {
        if (!storage.exists(id)) {
            throw new NotExistsException("Рейтинг", id);
        }
    }
}
