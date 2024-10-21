package com.ecc.nichole.registration.core.controller;


import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.dto.PersonDto;
import com.ecc.nichole.registration.core.model.dto.RoleDto;
import com.ecc.nichole.registration.core.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PersonDto personDto) {
        personService.create(personDto);
        return ResponseEntity.ok("Successfully saved person with UUID " + personDto.getUuid() + ".");
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons(
            @RequestParam(name = "sortBy", defaultValue = "name.lastName") String sortBy,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<PersonDto> persons = personService.getAll(sortBy, order, pageable);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{uuid}/roles")
    public ResponseEntity<List<RoleDto>> getAllRoles(
            @PathVariable UUID uuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return Optional.ofNullable(personService.getAllRoles(uuid, pageable))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot retrieve any roles for person with ID " + uuid + "."));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<String> update(@PathVariable UUID uuid, @RequestBody PersonDto person) {
        return Optional.ofNullable(personService.update(uuid, person))
            .map(updatedPerson -> ResponseEntity.ok("Person successfully updated."))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existing role with UUID " + uuid + "."));
    }

    @PatchMapping("/{uuid}/contact")
    public ResponseEntity<String> patchContactInformation(@PathVariable UUID uuid, @RequestBody ContactInformation contactInformation) {
        Optional<PersonDto> updatedPerson = personService.patchContactInformation(uuid, contactInformation);

        return updatedPerson
            .map(person -> ResponseEntity.ok("Successfully patched contact information of person with UUID " + uuid + "."))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot patch non-existing person with UUID " + uuid + "."));
    }

    @PatchMapping("/{uuid}/roles")
    public ResponseEntity<String> patchRoles(@PathVariable UUID uuid, @RequestBody List<RoleDto> role) {
        Optional<PersonDto> updatedPerson = personService.patchRoles(uuid, role);

        return updatedPerson
            .map(person -> ResponseEntity.ok("Successfully patched roles of person with UUID " + uuid + "."))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot patch non-existing person with UUID " + uuid + "."));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> delete(@PathVariable UUID uuid) {
        if (personService.delete(uuid)) {
            return ResponseEntity.ok("Person successfully deleted.");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete non-existing person with UUID " + uuid + ".");
        }
    }
}
