package ru.job4j.todo.service;

import org.springframework.stereotype.*;
import ru.job4j.todo.model.*;
import ru.job4j.todo.repository.*;

import java.util.*;

@Service
public class HbmUserService implements UserService{

    private final UserRepository userRepository;

    public HbmUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deleteById(int id) {
        return userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Collection<String> getTimeZones() {
        Collection<String> res = new ArrayList<>();
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        for (TimeZone zone : zones) {
            res.add(zone.getDisplayName());
        }
        return res;
    }
}
