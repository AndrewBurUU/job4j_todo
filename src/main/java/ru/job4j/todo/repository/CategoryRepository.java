package ru.job4j.todo.repository;

import ru.job4j.todo.model.*;

import java.util.*;

public interface CategoryRepository {
    Collection<Category> findAll();
}
