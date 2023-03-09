package ru.job4j.todo.repository;

import org.hibernate.*;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.junit.jupiter.api.*;
import ru.job4j.todo.model.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Disabled
public class HbmRepositoryTest {

    private static StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private static SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static HbmRepository hbmRepository;

    private static CrudRepository crudRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        crudRepository = new CrudRepository(sf);

        hbmRepository = new HbmRepository(crudRepository);

        var tasks = hbmRepository.findAll();
        for (var task  : tasks) {
            hbmRepository.deleteById(task.getId());
        }
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
        var task = new Task(1, "task1", creationDate, true, new User(), new Priority());
        task = hbmRepository.save(task);
        var savedTask = hbmRepository.findById(task.getId()).get();
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenSeveralThenGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = hbmRepository.save(new Task(1, "task1", creationDate, true, new User(), new Priority()));
        var task2 = hbmRepository.save(new Task(2, "task2", creationDate, true, new User(), new Priority()));
        var task3 = hbmRepository.save(new Task(3, "task3", creationDate, true, new User(), new Priority()));
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
        var task = hbmRepository.save(new Task(1, "task1", creationDate, true, new User(), new Priority()));
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
        var task = hbmRepository.save(new Task(1, "task1", creationDate, true, new User(), new Priority()));
        var updatedTask = new Task(task.getId(), "task1updated", creationDate.plusDays(1), true, new User(), new Priority());
        var isUpdated = hbmRepository.update(updatedTask);
        var savedTask = hbmRepository.findById(updatedTask.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(updatedTask);
    }

    @Test
    public void whenUpdateUnExistingTaskThenGetFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = hbmRepository.save(new Task(1, "task1", creationDate, true, new User(), new Priority()));
        var isUpdated = hbmRepository.update(new Task(2, "task2", creationDate, true, new User(), new Priority()));
        assertThat(isUpdated).isFalse();
    }
}