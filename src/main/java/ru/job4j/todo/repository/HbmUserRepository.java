package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;

import java.util.*;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository{

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> res = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
            res = Optional.of(user);
        } catch (Exception e) {
            throw e;
        }
        return res;
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.run("DELETE User WHERE id = :fId",
                Map.of("fId", id)) > 0;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("from User where login = :fLogin and password = :fPassword", User.class,
                Map.of("fLogin", login, "fPassword", password)
                );
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional("from User where id = :fId", User.class,
                Map.of("fId", id)
        );
    }

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("from User", User.class);
    }
}
