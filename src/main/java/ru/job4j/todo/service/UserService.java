package ru.job4j.todo.service;

import ru.job4j.todo.model.*;

import java.util.*;

public interface UserService {

    Optional<User> save(User user);

    boolean deleteById(int id);

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> findById(int id);

    Collection<User> findAll();

    Collection<UserTimeZone> getTimeZones();

}
