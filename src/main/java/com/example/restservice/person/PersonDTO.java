package com.example.restservice.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private String name;
    private int age;
    private String email;
    private double height;

    public Person toPerson(){
        return new Person(name, age, email, height);
    }
}
