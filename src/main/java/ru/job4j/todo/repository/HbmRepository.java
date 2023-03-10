package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbmRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public Task save(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.run("DELETE Task WHERE id = :fId",
                Map.of("fId", id)) > 0;
    }

    @Override
    public boolean update(Task task) {
        Task taskBefore = new Task(
                task.getId(),
                task.getDescription(),
                task.getCreated(),
                task.isDone(),
                task.getUser(),
                task.getPriority(),
                task.getCategories());
        crudRepository.run(session -> session.merge(task));
        return taskBefore.equals(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("from Task t JOIN FETCH t.priority where t.id = :fId", Task.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query("FROM Task t JOIN FETCH t.priority", Task.class);
    }

    @Override
    public Collection<Task> findNewOrDone(boolean done) {
        return crudRepository.query("from Task t JOIN FETCH t.priority where t.done = :fDone", Task.class,
                Map.of("fDone", done));
    }
}
