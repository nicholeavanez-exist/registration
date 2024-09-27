package com.ecc.nichole.registration.core.service.interfaces;

import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Person;
import com.ecc.nichole.registration.core.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonService {
    void create(Person person);
    Optional<Person> getById(Long id);
    List<Person> getAll(String sortBy, String order);
    List<Role> getAllRoles(Long id);
    Optional<Person> patchContactInformation(Long id, ContactInformation updatedContactInformation);
    Optional<Person> patchRoles(Long id, List<Role> updatedRole);
    Optional<Person> update(Long id, Person person);
    boolean delete(Long id);
}