package com.ecc.nichole.registration.core.service.interfaces;

import com.ecc.nichole.registration.core.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void create(Role role);

    Optional<Role> getById(Long id);

    List<Role> getAllById();

    void update(Long id, Role role);

    boolean delete(Long id);
}