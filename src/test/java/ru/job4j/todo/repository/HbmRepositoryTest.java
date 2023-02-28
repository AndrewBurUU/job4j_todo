package ru.job4j.todo.repository;

import org.hibernate.*;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.store.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HbmRepositoryTest {

    private static HbmRepository hbmRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();

        hbmRepository = new HbmRepository(new TaskStore(sf));
    }

    @AfterEach
    public void clearTasks() {
        var tasks = hbmRepository.findAll();
        for (var task  : tasks) {
            hbmRepository.deleteById(task.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = hbmRepository.save(new Task(1, "task1", creationDate, false));
        var savedTask = hbmRepository.findById(task.getId()).get();
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenSeveralThenGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = hbmRepository.save(new Task(1, "task1", creationDate, false));
        var task2 = hbmRepository.save(new Task(2, "task2", creationDate, false));
        var task3 = hbmRepository.save(new Task(3, "task3", creationDate, false));
        var result = hbmRepository.findAll();
        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(hbmRepository.findAll()).isEqualTo(emptyList());
        assertThat(hbmRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = hbmRepository.save(new Task(1, "task1", creationDate, false));
        var isDeleted = hbmRepository.deleteById(task.getId());
        var savedTask = hbmRepository.findById(task.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTask).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(hbmRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = hbmRepository.save(new Task(1, "task1", creationDate, false));
        var updatedTask = new Task(task.getId(), "task1updated", creationDate.plusDays(1), true);
        var isUpdated = hbmRepository.update(updatedTask);
        var savedTask = hbmRepository.findById(updatedTask.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(updatedTask);
    }

    @Test
    public void whenUpdateUnExistingTaskThenGetFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = hbmRepository.save(new Task(1, "task1", creationDate, false));
        var isUpdated = hbmRepository.update(new Task(2, "task2", creationDate, true));
        assertThat(isUpdated).isFalse();
    }
}