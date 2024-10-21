package com.ecc.nichole.registration.core.service;

import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.dto.PersonDto;
import com.ecc.nichole.registration.core.model.dto.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonService {
    void create(PersonDto person);
    Optional<PersonDto> getByUuid(UUID uuid);
    List<PersonDto> getAll(String sortBy, String order, Pageable pageable);
    List<RoleDto> getAllRoles(UUID uuid, Pageable pageable);
    Optional<PersonDto> patchContactInformation(UUID uuid, ContactInformation updatedContactInformation);
    Optional<PersonDto> patchRoles(UUID uuid, List<RoleDto> updatedRole);
    Optional<PersonDto> update(UUID uuid, PersonDto person);
    boolean delete(UUID uuid);
}