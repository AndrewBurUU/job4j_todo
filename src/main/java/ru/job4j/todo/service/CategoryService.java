package ru.job4j.todo.service;

import ru.job4j.todo.model.*;

import java.util.*;

public interface CategoryService {
    Collection<Category> findAll();
}
