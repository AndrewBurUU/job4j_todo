package ru.job4j.todo.repository.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Data
public class TaskStore {

    private final SessionFactory sf;

}