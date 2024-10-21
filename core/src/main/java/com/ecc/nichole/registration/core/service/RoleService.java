package com.ecc.nichole.registration.core.service;

import com.ecc.nichole.registration.core.model.dto.RoleDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {

    void create(RoleDto role);

    Optional<RoleDto> getByUuid(UUID uuid);

    List<RoleDto> getAllByUuid();

    void update(UUID uuid, RoleDto role);

    boolean delete(UUID uuid);
}