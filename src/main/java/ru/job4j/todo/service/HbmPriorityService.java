package ru.job4j.todo.service;

import lombok.*;
import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.*;

import java.util.*;

@Service
@AllArgsConstructor
public class HbmPriorityService implements PriorityService{

    private final PriorityRepository priorityRepository;

    @Override
    public Collection<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
