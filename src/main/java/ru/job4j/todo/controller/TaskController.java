package ru.job4j.todo.controller;

import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.service.*;

import javax.servlet.http.*;
import java.util.*;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

    @GetMapping("/all")
    public String getAll(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var taskAll = taskService.findAll();
        var tasks = taskService.setUserZone(user, taskAll);
        model.addAttribute("tasks", tasks);
        return "tasks/all";
    }

    @GetMapping("/done")
    public String getByDone(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var taskDone = taskService.findNewOrDone(true);
        var tasks = taskService.setUserZone(user, taskDone);
        model.addAttribute("tasks", tasks);
        return "tasks/done";
    }

    @GetMapping("/new")
    public String getNew(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        var taskNew = taskService.findNewOrDone(false);
        var tasks = taskService.setUserZone(user, taskNew);
        model.addAttribute("tasks", tasks);
        return "tasks/new";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model, HttpSession session, HttpServletRequest req) {
        var user = (User) session.getAttribute("user");
        task.setUser(user);
        String[] cat = req.getParameterValues("selected_categories");
        ArrayList<Category> categories = new ArrayList<>();
        for (String s : cat) {
            Category category = new Category();
            category.setId(Integer.parseInt(s));
            categories.add(category);
        }
        task.setCategories(categories);
        try {
            taskService.save(task);
            return "redirect:/tasks/all";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/detail";
    }

    @GetMapping("/done/{id}")
    public String doneById(Model model, @PathVariable int id) {
        var isDone = taskService.doneById(id);
        if (!isDone) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        return "redirect:/tasks/all";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        return "redirect:/tasks/all";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, HttpSession session) {
        try {
            var user = (User) session.getAttribute("user");
            task.setUser(user);
            var isUpdated = taskService.update(task);
            if (!isUpdated) {
                model.addAttribute("message", "Задание с указанным идентификатором не найдено");
                return "errors/404";
            }
            return "redirect:/tasks/all";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

}
