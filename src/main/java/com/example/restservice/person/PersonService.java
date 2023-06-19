package com.example.restservice.person;

import com.example.restservice.exception.BadRequestEx;
import com.example.restservice.exception.PersonAlreadyExistEx;
import com.example.restservice.exception.PersonNotFoundEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService{
    @Autowired
    PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public List<Person> getPersonsFilter(Optional<String> name, Optional<String> email, Optional<Integer> age, Optional<Integer> height){
        return personRepository.findAll().stream()
                .filter(p -> height.isEmpty() || p.getHeight() == height.get())
                .filter(p -> age.isEmpty() || p.getAge() == age.get())
                .filter(p -> email.isEmpty() || p.getEmail().toLowerCase().contains(email.get().toLowerCase()))
                .filter(p -> name.isEmpty() || p.getName().toLowerCase().contains(name.get().toLowerCase()))
                .collect(Collectors.toList());
    }
    public Person getPerson(int id) throws PersonNotFoundEx {
        for (Person p:personRepository.findAll()){
            if (p.getId() == id)
                return p;
        }
        throw new PersonNotFoundEx(id);
    }

    public Person updatePerson(int personID, PersonDTO person) throws PersonNotFoundEx {
        for (Person p:personRepository.findAll()){
            if (p.getId() == personID){
                if (person.getAge() > 0)
                    p.setAge(person.getAge());
                if (person.getName() != null && !person.getName().isEmpty() && !person.getName().isBlank())
                    p.setName(person.getName());
                if (person.getEmail() != null && !person.getEmail().isEmpty() && !person.getEmail().isBlank())
                    p.setEmail(person.getEmail());
                personRepository.save(p);
                return p;
            }
        }
        throw new PersonNotFoundEx(personID);
    }

    public boolean deletePerson(int id) throws PersonNotFoundEx {
        for (Person p:personRepository.findAll()){
            if (p.getId() == id) {
                personRepository.delete(p);
                return true;
            }
        }
        throw new PersonNotFoundEx(id);
    }

    public Person addPerson(PersonDTO person) throws BadRequestEx, PersonAlreadyExistEx {
        checkInputs(person.getName(), person.getEmail(), person.getAge(), person.getHeight());
        for (Person p:personRepository.findAll()){
            if (p.getEmail().equals(person.getEmail())) throw new PersonAlreadyExistEx("Person already exists.");
        }
        Person newPerson = new Person(person.getName(), person.getAge(), person.getEmail(), person.getHeight());
        personRepository.save(newPerson);
        return newPerson;
    }

    public long countPersons(){
        return personRepository.count();
    }

    private void checkInputs(String name, String email, int age, double height)  throws BadRequestEx{
        if (name == null || name.isEmpty() || name.isBlank())
            throw new BadRequestEx("Invalid input. No name content.");
        if (email == null || email.isEmpty() || email.isBlank())
            throw new BadRequestEx("Invalid input. No email content.");
        if (age <=0)
            throw new BadRequestEx("Invalid age input.");
        if (height <=0)
            throw new BadRequestEx("Invalid height input.");
    }
}
