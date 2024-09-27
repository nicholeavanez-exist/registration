package com.ecc.nichole.registration.core.controller;


import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Person;
import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.service.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonServiceImpl personServiceImpl;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Person person) {
        personServiceImpl.create(person);
        return ResponseEntity.ok("Successfully saved person.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Person>> getById(@PathVariable Long id) {
        return Optional.ofNullable(personServiceImpl.getById(id))
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot retrieve non-existing person with ID " + id + "."));
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons(
        @RequestParam(name = "sortBy", defaultValue = "name.lastName") String sortBy,
        @RequestParam(name = "order", defaultValue = "asc") String order) {

        List<Person> persons = personServiceImpl.getAll(sortBy, order);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<Role>> getAllRoles(@PathVariable Long id) {
        return Optional.ofNullable(personServiceImpl.getAllRoles(id))
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot retrieve any roles for person with ID " + id + "."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Person person) {
        return Optional.ofNullable(personServiceImpl.update(id, person))
            .map(updatedPerson -> ResponseEntity.ok("Person successfully updated."))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existing role with ID " + id + "."));
    }

    @PatchMapping("/{id}/contact")
    public ResponseEntity<String> patchContactInformation(@PathVariable Long id, @RequestBody ContactInformation contactInformation) {
        Optional<Person> updatedPerson = personServiceImpl.patchContactInformation(id, contactInformation);

        return updatedPerson
            .map(person -> ResponseEntity.ok("Successfully patched contact information of person with ID " + id + "."))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot patch non-existing person with ID " + id + "."));
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<String> patchRoles(@PathVariable Long id, @RequestBody List<Role> role) {
        Optional<Person> updatedPerson = personServiceImpl.patchRoles(id, role);

        return updatedPerson
            .map(person -> ResponseEntity.ok("Successfully patched roles of person with ID " + id + "."))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot patch non-existing person with ID " + id + "."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (personServiceImpl.delete(id)) {
            return ResponseEntity.ok("Person successfully deleted.");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete non-existing person with ID " + id + ".");
        }
    }
}
