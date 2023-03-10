package ru.job4j.todo.service;

import org.junit.jupiter.api.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.*;

import java.time.temporal.*;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class HbmTaskServiceTest {

    private HbmTaskService hbmTaskService;

    private TaskRepository taskRepository;

    @BeforeEach
    public void initService() {
        taskRepository = mock(TaskRepository.class);
        hbmTaskService = new HbmTaskService(taskRepository);
    }

    @Test
    public void whenCreateNewOneThenSaveTask() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.save(task)).thenReturn(task);

        var result = hbmTaskService.save(task);
        assertThat(result).isEqualTo(task);
    }

    @Test
    public void whenDeleteByIdThenTrue() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.deleteById(task.getId())).thenReturn(true);

        var result = hbmTaskService.deleteById(task.getId());
        assertThat(result).isTrue();
    }

    @Test
    public void whenNothingDeleteByIdThenFalse() {
        var result = hbmTaskService.deleteById(0);
        assertThat(result).isFalse();
    }

    @Test
    public void whenUpdateThenTrue() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = taskRepository.save(new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of()));
        var updatedTask = new Task(1, "task1updated", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.update(updatedTask)).thenReturn(true);

        var result = hbmTaskService.update(updatedTask);
        assertThat(result).isTrue();
    }

    @Test
    public void whenIncorrectTaskUpdateThenFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = taskRepository.save(new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of()));
        var updatedTask = new Task(2, "task1updated", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.update(updatedTask)).thenReturn(false);

        var result = hbmTaskService.update(updatedTask);
        assertThat(result).isFalse();
    }

    @Test
    public void whenFindByIdThenTask() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        var result = hbmTaskService.findById(task.getId()).get();
        assertThat(result).isEqualTo(task);
    }

    @Test
    public void whenFindNewThenNewTaskList() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = new Task(1, "task1", creationDate, false, new User(), new Priority(), List.of());
        var task2 = new Task(2, "task2", creationDate, false, new User(), new Priority(), List.of());
        var task3 = new Task(3, "task3", creationDate.minusDays(1), true, new User(), new Priority(), List.of());
        var taskList = List.of(task1, task2);
        when(taskRepository.findNewOrDone(false)).thenReturn(taskList);

        var result = hbmTaskService.findNewOrDone(false);
        assertThat(result).isEqualTo(taskList);
    }

    @Test
    public void whenFindByDoneThenDoneTaskList() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        var task2 = new Task(2, "task2", creationDate, true, new User(), new Priority(), List.of());
        var task3 = new Task(3, "task3", creationDate.minusDays(1), false, new User(), new Priority(), List.of());
        var taskList = List.of(task1, task2);
        when(taskRepository.findNewOrDone(true)).thenReturn(taskList);

        var result = hbmTaskService.findNewOrDone(true);
        assertThat(result).isEqualTo(taskList);
    }

    @Test
    public void whenDoneByIdThenTrue() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.update(task)).thenReturn(true);

        var result = hbmTaskService.doneById(task.getId());
        assertThat(result).isTrue();
    }

    @Test
    public void whenDoneByIdNotFoundThenFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        when(taskRepository.update(task)).thenReturn(false);

        var result = hbmTaskService.doneById(task.getId());
        assertThat(result).isFalse();
    }

}