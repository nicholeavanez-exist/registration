package com.ecc.nichole.registration.core.controller;

import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        roleServiceImpl.create(role);
        return ResponseEntity.ok("Successfully saved role " + role.getName() + " with ID " + role.getId()+ ".") ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Role>> getRole(@PathVariable Long id) {
        return Optional.ofNullable(roleServiceImpl.getById(id))
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot retrieve non-existing role with ID " + id + "."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody Role updatedRole) {
        return roleServiceImpl.getById(id)
            .map(existingRole -> {
                existingRole.setName(updatedRole.getName());
                roleServiceImpl.update(id, existingRole);
                return ResponseEntity.ok("Successfully updated role " + existingRole.getName() + " with ID " + id + ".");
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existing role with ID " + id + "."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        return roleServiceImpl.getById(id)
            .map(role -> {
                if (roleServiceImpl.delete(id)) {
                    return ResponseEntity.ok("Successfully deleted role " + role.getName() + " with ID " + role.getId() + ".");
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete role with ID " + id + ".");
                }
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete non-existing role with ID " + id + "."));
    }
}