package ru.job4j.todo.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;
import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String name;

    private String login;

    private String password;

    private String timezone;
}
