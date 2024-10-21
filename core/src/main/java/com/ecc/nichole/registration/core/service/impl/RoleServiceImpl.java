package com.ecc.nichole.registration.core.service.impl;

import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.model.dto.RoleDto;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.RoleService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void create(RoleDto role) {
        roleRepository.save(fromDto(role));
    }

    @Override
    public Optional<RoleDto> getByUuid(UUID uuid) {
        return Optional.ofNullable(toDto(roleRepository.findByUuid(uuid).orElse(null)));
    }

    @Override
    public void update(UUID uuid, RoleDto role) {
        if (roleRepository.existsByUuid(uuid)) {
            roleRepository.save(fromDto(role));
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        if (roleRepository.existsByUuid(uuid)) {
            roleRepository.deleteByUuid(uuid);
            return true;
        }
        return false;
    }

    @Override
    public List<RoleDto> getAllByUuid() {
        return roleRepository.findAll()
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public RoleDto toDto(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleDto(
            role.getId(),
            role.getUuid(),
            role.getName()
        );
    }

    public Role fromDto(RoleDto roleDto) {
        if (roleDto == null) {
            return null;
        }

        Role role = new Role();
        role.setId(roleDto.getId());

        if(roleDto.getUuid() != null) {
            role.setUuid(roleDto.getUuid());
        }

        role.setName(roleDto.getName());
        return role;
    }
}