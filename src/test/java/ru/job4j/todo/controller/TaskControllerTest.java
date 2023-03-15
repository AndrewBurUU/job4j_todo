package ru.job4j.todo.controller;

import org.junit.jupiter.api.*;
import org.springframework.ui.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.service.*;

import javax.servlet.http.*;
import java.time.temporal.*;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@Disabled
class TaskControllerTest {

    private TaskService taskService;

    private TaskController taskController;

    private PriorityService priorityService;

    private CategoryService categoryService;

    private HttpSession session;

    private HttpServletRequest req;

    @BeforeEach
    public void initServices() {
        taskService = mock(TaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        session = mock(HttpSession.class);
        req = mock(HttpServletRequest.class);
        taskController = new TaskController(taskService, priorityService, categoryService);
    }

    @Test
    public void whenRequestAllPageThenGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = new Task(1, "task1", creationDate, false, new User(), new Priority(), List.of());
        var task2 = new Task(2, "task2", creationDate, true, new User(), new Priority(), List.of());
        var task3 = new Task(3, "task3", creationDate, false, new User(), new Priority(), List.of());
        var result = taskService.findAll();
        when(result).thenReturn(List.of(task1, task2, task3));

        var model = new ConcurrentModel();
        var view = taskController.getAll(model, session);

        assertThat(view).isEqualTo("tasks/all");
    }

    @Test
    public void whenRequestDonePageThenGetDone() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        var task2 = new Task(2, "task2", creationDate, true, new User(), new Priority(), List.of());
        var task3 = new Task(3, "task3", creationDate, false, new User(), new Priority(), List.of());
        var result = taskService.findNewOrDone(true);
        when(result).thenReturn(List.of(task1, task2));

        var model = new ConcurrentModel();
        var view = taskController.getByDone(model, session);

        assertThat(view).isEqualTo("tasks/done");
    }

    @Test
    public void whenRequestNewPageThenGetNew() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var task1 = new Task(1, "task1", creationDate, true, new User(), new Priority(), List.of());
        var task2 = new Task(2, "task2", creationDate, true, new User(), new Priority(), List.of());
        var task3 = new Task(3, "task3", creationDate.minusDays(1), false, new User(), new Priority(), List.of());
        var result = taskService.findNewOrDone(false);
        when(result).thenReturn(List.of(task1, task2));

        var model = new ConcurrentModel();
        var view = taskController.getNew(model, session);

        assertThat(view).isEqualTo("tasks/new");
    }

    @Test
    public void whenRequestCreatePageThenCreate() {
        var model = new ConcurrentModel();
        var view = taskController.getCreationPage(model);

        assertThat(view).isEqualTo("tasks/create");
    }

    @Test
    public void whenPostTaskThenSameDataAndRedirectToAllPage() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var user = new User();
        var priority = new Priority();
        var task = new Task(1, "task1", creationDate, true, user, priority, List.of());
        var result = taskService.save(task);
        when(result).thenReturn(task);

        var model = new ConcurrentModel();
        var view = taskController.create(task, model, session, req);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

}