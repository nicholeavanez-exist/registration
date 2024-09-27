package com.ecc.nichole.registration.core.service.impl;

import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void create(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Optional<Role> getById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void update(Long id, Role role) {
        if (roleRepository.existsById(id)) {
            roleRepository.save(role);
        }
    }

    @Override
    public boolean delete(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Role> getAllById() {
        return roleRepository.findAll();
    }
}