package ru.job4j.todo.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "priorities")
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String name;

    private int position;
}