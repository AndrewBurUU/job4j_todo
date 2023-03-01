package ru.job4j.todo.repository;

import org.hibernate.*;

import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.store.*;

import java.util.*;

@Repository
public class HbmUserRepository implements UserRepository{

    private final TaskStore taskStore;

    public HbmUserRepository(TaskStore taskStore) {
        this.taskStore = taskStore;
    }

    @Override
    public Optional<User> save(User user) {
        Session session = taskStore.getSf().openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return Optional.of(user);
    }

    @Override
    public boolean deleteById(int id) {
        Session session = taskStore.getSf().openSession();
        int count = 0;
        try {
            session.beginTransaction();
            count = session.createQuery("DELETE User WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return count > 0;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = taskStore.getSf().openSession();
        Optional<User> user = session.createQuery("from User where login = :fLogin and password = :fPassword", User.class)
                .setParameter("fLogin", login)
                .setParameter("fPassword", password)
                .uniqueResultOptional();
        session.close();
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        Session session = taskStore.getSf().openSession();
        Optional<User> user = session.createQuery("from User where id = :fId", User.class)
                .setParameter("fId", id)
                .uniqueResultOptional();
        session.close();
        return user;
    }

    @Override
    public Collection<User> findAll() {
        Session session = taskStore.getSf().openSession();
        List<User> res = session.createQuery("from User", User.class).list();
        session.close();
        return res;
    }
}
