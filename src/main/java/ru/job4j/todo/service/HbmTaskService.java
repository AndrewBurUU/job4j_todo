package ru.job4j.todo.service;

import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.*;

import java.sql.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

@Service
public class HbmTaskService implements TaskService {

    private final TaskRepository taskRepository;

    public HbmTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Collection<Task> findNewOrDone(boolean done) {
        return taskRepository.findNewOrDone(done);
    }

    @Override
    public boolean doneById(int id) {
        var taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty())
            return false;
        taskOptional.get().setDone(true);
        return taskRepository.update(taskOptional.get());
    }

    @Override
    public Collection<Task> setUserZone(User user, Collection<Task> tasks) {
        Collection<Task> res = new ArrayList<>();
        for (Task task : tasks) {
            task.setCreated(setSelectedTZ(user, task));
            res.add(task);
        }
        return res;
    }

    public static LocalDateTime setSelectedTZ(User user, Task task) {
        ZonedDateTime zonedDateTime;
        if (user.getTimezone() == null) {
            zonedDateTime = ZonedDateTime.of(task.getCreated(), TimeZone.getDefault().toZoneId());
        } else {
            zonedDateTime = ZonedDateTime.of(task.getCreated(),
            TimeZone.getDefault().toZoneId()).withZoneSameInstant(ZoneId.of(String.valueOf(user.getTimezone())));
        }
        return zonedDateTime.toLocalDateTime();
    }
}
