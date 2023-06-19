package com.example.restservice.person;

import com.example.restservice.exception.BadRequestEx;
import com.example.restservice.exception.PersonAlreadyExistEx;
import com.example.restservice.exception.PersonNotFoundEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/persons",
        produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE})
@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Person> getPerson(@PathVariable int id) throws PersonNotFoundEx {
        System.out.println("...wywołano getPerson");
        Person p = personService.getPerson(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(p);
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Person>> getPersons(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Integer> age,
            @RequestParam(required = false) Optional<Integer> height){
        System.out.println("...wywołano getPersons");
        List<Person> list;
        if (name.isEmpty() && email.isEmpty() && age.isEmpty())
            list = personService.getAllPersons();
        else
            list = personService.getPersonsFilter(name, email, age, height);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Person> addPerson(@RequestBody PersonDTO person) throws BadRequestEx, PersonAlreadyExistEx {
        System.out.println("...wywołano addPerson");
        Person p = personService.addPerson(person);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(p);
    }

    @RequestMapping(value = "/{personID}", method = RequestMethod.PUT)
    public ResponseEntity<Person> updatePerson(@PathVariable int personID, @RequestBody PersonDTO person)
            throws PersonNotFoundEx, BadRequestEx{
        System.out.println("...wywołano updatePerson");
        Person p = personService.updatePerson(personID, person);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(p);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePerson(@PathVariable int id) throws PersonNotFoundEx{
        System.out.println("...wywołano deletePerson");
        boolean b = personService.deletePerson(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("{\"val\": \"OK\"}");
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseEntity<CountPerson> countPersons(){
        System.out.println("...wywołano countPerson");
        CountPerson c = new CountPerson(personService.countPersons());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(c);
    }
}
