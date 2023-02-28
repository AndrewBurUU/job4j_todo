package ru.job4j.todo.repository;

import org.hibernate.*;

import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.store.*;

import java.util.*;

@Repository
public class HbmRepository implements TaskRepository {

/**
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
*/
    private final TaskStore taskStore;

    public HbmRepository(TaskStore taskStore) {
        this.taskStore = taskStore;
    }

    @Override
    public Task save(Task task) {
        Session session = taskStore.getSf().openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        Session session = taskStore.getSf().openSession();
        int count = 0;
        try {
            session.beginTransaction();
            count = session.createQuery("DELETE Task WHERE id = :fId")
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
    public boolean update(Task task) {
        Session session = taskStore.getSf().openSession();
        int count = 0;
        try {
            session.beginTransaction();
            count = session.createQuery("UPDATE Task "
                            + "SET description = :fDescription, "
                            + "created = :fCreated, "
                            + "done = :fDone "
                            + "WHERE id = :fId")
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return count > 0;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = taskStore.getSf().openSession();
        Optional<Task> task = session.createQuery("from Task where id = :fId", Task.class)
                .setParameter("fId", id)
                .uniqueResultOptional();
        session.close();
        return task;
    }

    @Override
    public Collection<Task> findAll() {
        Session session = taskStore.getSf().openSession();
        List<Task> res = session.createQuery("from Task", Task.class).list();
        session.close();
        return res;
    }

    @Override
    public Collection<Task> findNew() {
        Session session = taskStore.getSf().openSession();
        List<Task> res = session.createQuery("from Task where created >= CURRENT_DATE", Task.class).list();
        session.close();
        return res;
    }

    @Override
    public Collection<Task> findByDone() {
        Session session = taskStore.getSf().openSession();
        List<Task> res = session.createQuery("from Task where done = true", Task.class).list();
        session.close();
        return res;
    }

}
