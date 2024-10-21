package com.ecc.nichole.registration.core.controller;

import com.ecc.nichole.registration.core.model.dto.RoleDto;
import com.ecc.nichole.registration.core.service.RoleService;
import com.ecc.nichole.registration.core.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> createRole(@RequestBody RoleDto roleDto) {
        roleService.create(roleDto);
        return ResponseEntity.ok("Successfully saved role " + roleDto.getName() + " with UUID " + roleDto.getUuid() + ".") ;
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> getRole(@PathVariable UUID uuid) {
        RoleDto role = roleService.getByUuid(uuid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot retrieve non-existing role with UUID " + uuid + "."));

        return ResponseEntity.ok(role);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> updateRole(@PathVariable UUID uuid, @RequestBody RoleDto updatedRole) {
        return roleService.getByUuid(uuid)
            .map(existingRole -> {
                existingRole.setName(updatedRole.getName());
                roleService.update(uuid, existingRole);
                return ResponseEntity.ok("Successfully updated role " + existingRole.getName() + " with UUID " + uuid + ".");
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existing role with UUID " + uuid + "."));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> deleteRole(@PathVariable UUID uuid) {
        return roleService.getByUuid(uuid)
            .map(role -> {
                if (roleService.delete(uuid)) {
                    return ResponseEntity.ok("Successfully deleted role " + role.getName() + " with UUID " + uuid + ".");
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete role with UUID " + uuid + ".");
                }
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete non-existing role with UUID " + uuid + "."));
    }
}