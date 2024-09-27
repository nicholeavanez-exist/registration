package com.ecc.nichole.registration.core.service.impl;

import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Person;
import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.repo.PersonRepository;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.interfaces.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void create(Person person) {
        personRepository.save(person);
    }

    @Override
    public Optional<Person> getById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> getAll(String sortBy, String orderBy) {
        Sort sort = Sort.by(Sort.Direction.fromString(orderBy), sortBy);
        return personRepository.findAll(sort);
    }

    @Override
    public List<Role> getAllRoles(Long id) {
        return personRepository.findById(id)
            .map(person -> new ArrayList<>(person.getRoles()))
            .orElseThrow(() -> new EntityNotFoundException("Cannot retrieve non-existing person with ID " + id + "."));
    }

    @Override
    public Optional<Person> patchContactInformation(Long id, ContactInformation updatedContactInformation) {
        return personRepository.findById(id)
            .map(existingPerson -> {
                ContactInformation currentContactInformation = existingPerson.getContactInformation();

                ContactInformation updatedContactInfo = currentContactInformation.toBuilder()
                    .email(updatedContactInformation.getEmail())
                    .landline(updatedContactInformation.getLandline())
                    .mobileNumber(updatedContactInformation.getMobileNumber())
                    .build();

                Person updatedPerson = existingPerson.toBuilder()
                    .contactInformation(updatedContactInfo)
                    .build();

                return personRepository.save(updatedPerson);
            });
    }

    @Override
    public Optional<Person> patchRoles(Long id, List<Role> updatedRoles) {
        return personRepository.findById(id)
            .map(existingPerson -> {
                Set<Long> roleIds = updatedRoles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

                Set<Role> validRoles = new HashSet<>(roleRepository.findAllById(roleIds));

                Set<Long> invalidRoleIds = roleIds.stream()
                    .filter(roleId -> validRoles.stream().noneMatch(role -> role.getId().equals(roleId)))
                    .collect(Collectors.toSet());

                if (!invalidRoleIds.isEmpty()) {
                    String invalidIds = invalidRoleIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update person's role for non-existing IDs: " + invalidIds);
                }

                Person updatedPerson = existingPerson.toBuilder()
                    .roles(validRoles)
                    .build();

                return personRepository.save(updatedPerson);
            });
    }

    @Override
    public Optional<Person> update(Long id, Person updatedPerson) {
        return personRepository.findById(id)
            .map(existingPerson -> {
                existingPerson.setName(updatedPerson.getName());
                existingPerson.setAddress(updatedPerson.getAddress());
                existingPerson.setBirthDate(updatedPerson.getBirthDate());
                existingPerson.setGwa(updatedPerson.getGwa());
                existingPerson.setHireDate(updatedPerson.getHireDate());
                existingPerson.setEmployed(updatedPerson.isEmployed());
                existingPerson.setContactInformation(updatedPerson.getContactInformation());

                Set<Role> updatedRoles = updatedPerson.getRoles().stream()
                        .map(role -> roleRepository.findById(role.getId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ID " + role.getId() + " does not exist.")))
                        .collect(Collectors.toSet());

                existingPerson.setRoles(updatedRoles);

                return personRepository.save(existingPerson);
            });
    }

    @Override
    public boolean delete(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
}