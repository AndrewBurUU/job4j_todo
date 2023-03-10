package ru.job4j.todo.repository;

import lombok.*;
import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository{

    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> findAll() {
        return crudRepository.query("from Category", Category.class);
    }
}
