package com.ecc.nichole.registration.core.service.impl;

import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Person;
import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.model.dto.PersonDto;
import com.ecc.nichole.registration.core.model.dto.RoleDto;
import com.ecc.nichole.registration.core.repo.PersonRepository;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public void create(PersonDto person) {
        personRepository.save(fromDto(person));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public Optional<PersonDto> getByUuid(UUID uuid) {
        return Optional.ofNullable(toDto(personRepository.findByUuid(uuid).orElse(null)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public List<PersonDto> getAll(String sortBy, String orderBy, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.fromString(orderBy), sortBy);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return personRepository.findAll(pageable)
            .map(this::toDto)
            .stream().collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'VIEWER')")
    public List<RoleDto> getAllRoles(UUID uuid, Pageable pageable) {
        PersonDto person = toDto(
            personRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Cannot retrieve non-existing person with ID " + uuid + "."))
        );

        return person.getRoles().stream()
            .map(role -> new RoleServiceImpl().toDto(role))
            .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<PersonDto> patchContactInformation(UUID uuid, ContactInformation updatedContactInformation) {
        return personRepository.findByUuid(uuid)
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
            })
            .map(this::toDto);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<PersonDto> patchRoles(UUID uuid, List<RoleDto> updatedRoles) {
        return personRepository.findByUuid(uuid)
            .map(existingPerson -> {
                Set<Long> roleIds = updatedRoles.stream()
                    .map(RoleDto::getId)
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

                return toDto(personRepository.save(updatedPerson));
            });
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<PersonDto> update(UUID uuid, PersonDto updatedPerson) {
        return personRepository.findByUuid(uuid)
            .map(existingPerson -> {
                existingPerson.setName(updatedPerson.getName());
                existingPerson.setAddress(updatedPerson.getAddress());
                existingPerson.setBirthDate(updatedPerson.getBirthDate());
                existingPerson.setGwa(updatedPerson.getGwa());
                existingPerson.setHireDate(updatedPerson.getHireDate());
                existingPerson.setEmployed(updatedPerson.isEmployed());
                existingPerson.setContactInformation(updatedPerson.getContactInformation());

                Set<Role> updatedRoles = updatedPerson.getRoles().stream()
                    .map(role -> roleRepository.findByUuid(role.getUuid())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ID " + role.getUuid() + " does not exist.")))
                    .collect(Collectors.toSet());

                existingPerson.setRoles(updatedRoles);

                return toDto(personRepository.save(existingPerson));
            });
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public boolean delete(UUID uuid) {
        if (personRepository.existsByUuid(uuid)) {
            personRepository.deleteByUuid(uuid);
            return true;
        }
        return false;
    }

    public PersonDto toDto(Person person) {
        if (person == null) {
            return null;
        }

        return new PersonDto(
            person.getId(),
            person.getUuid(),
            person.getName(),
            person.getAddress(),
            person.getBirthDate(),
            person.getGwa(),
            person.getHireDate(),
            person.isEmployed(),
            person.getContactInformation(),
            person.getRoles(),
            person.getCreatedAt(),
            person.getUpdatedAt()
        );
    }

    public Person fromDto(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        Person person = new Person();
        person.setId(personDto.getId());

        if (personDto.getUuid() != null) {
            person.setUuid(personDto.getUuid());
        }

        person.setName(personDto.getName());
        person.setAddress(personDto.getAddress());
        person.setBirthDate(personDto.getBirthDate());
        person.setGwa(personDto.getGwa());
        person.setHireDate(personDto.getHireDate());
        person.setEmployed(personDto.isEmployed());
        person.setContactInformation(personDto.getContactInformation());
        person.setRoles(personDto.getRoles());

        if (personDto.getCreatedAt() != null) {
            person.setCreatedAt(personDto.getCreatedAt());
        }

        if (personDto.getUpdatedAt() != null) {
            person.setUpdatedAt(personDto.getUpdatedAt());
        }
        return person;
    }
}