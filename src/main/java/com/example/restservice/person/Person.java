package com.example.restservice.person;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;
    private String email;
    private double height;

    public Person(String name, int age, String email, double height) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.height = height;
    }
}
