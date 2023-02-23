package ru.job4j.todo.repository.store;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TaskStore {

    private final SessionFactory sf;

}