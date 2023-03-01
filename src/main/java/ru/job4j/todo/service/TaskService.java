package ru.job4j.todo.service;

import ru.job4j.todo.model.*;

import java.util.*;

public interface TaskService {

    Task save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    Collection<Task> findNewOrDone(boolean done);

    boolean doneById(int id);

}
