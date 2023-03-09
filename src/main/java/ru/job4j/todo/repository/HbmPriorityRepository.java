package ru.job4j.todo.repository;

import lombok.*;
import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbmPriorityRepository implements PriorityRepository{

    private final CrudRepository crudRepository;

    @Override
    public Collection<Priority> findAll() {
        return crudRepository.query("from Priority", Priority.class);
    }
}
