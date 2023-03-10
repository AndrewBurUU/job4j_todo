package ru.job4j.todo.service;

import lombok.*;
import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.*;

import java.util.*;

@Service
@AllArgsConstructor
public class HbmCategoryService implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Collection<Category> findAll() {
        return categoryRepository.findAll();
    }
}
