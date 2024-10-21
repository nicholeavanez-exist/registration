package com.ecc.nichole.registration.core.service;

import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.model.dto.RoleDto;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RoleServiceImplTest {

	@Mock
	private RoleRepository roleRepository;

    @Spy
	@InjectMocks
	private RoleServiceImpl roleService;

    private Role role;

	private RoleDto roleDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
        role = new Role();
		roleDto = roleService.toDto(role);

        doReturn(role).when(roleService).fromDto(roleDto);
        doReturn(roleDto).when(roleService).toDto(role);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Admin", "Developer", "QA", "BA"})
	void shouldCreateRole(String roleName) {
		roleDto.setName(roleName);

		when(roleRepository.save(any(Role.class))).thenReturn(role);
		roleService.create(roleDto);

		assertEquals(roleName, roleDto.getName(), "Created role name is not equal to the value source passed.");
		verify(roleRepository).save(role);
	}

	@Test
	void shouldGetExistingRole() {
		when(roleRepository.findByUuid(role.getUuid())).thenReturn(Optional.of(role));

		Optional<RoleDto> retrievedRole = roleService.getByUuid(role.getUuid());

		verify(roleRepository).findByUuid(role.getUuid());
		assertTrue(retrievedRole.isPresent(), "Retrieved role should be present.");
		assertEquals(roleDto.getId(), retrievedRole.get().getId(), "Retrieved role ID does not match.");
		assertEquals(roleDto.getName(), retrievedRole.get().getName(), "Retrieved role name does not match.");
	}

	@Test
	void shouldNotGetNonExistingRole() {
		when(roleRepository.findByUuid(role.getUuid())).thenReturn(Optional.empty());

		Optional<RoleDto> foundRole = roleService.getByUuid(role.getUuid());

		assertFalse(foundRole.isPresent(), "Found role should not be present for ID: " + role.getUuid());
		verify(roleRepository).findByUuid(role.getUuid());
	}

	@Test
	void shouldUpdateExistingRole() {
		when(roleRepository.existsByUuid(role.getUuid())).thenReturn(true);
		when(roleRepository.save(any(Role.class))).thenReturn(role);

		roleService.update(role.getUuid(), roleDto);

		verify(roleRepository).save(role);
	}

	@Test
	void shouldNotUpdateNonExistingRole() {
		when(roleRepository.existsByUuid(role.getUuid())).thenReturn(false);

		roleService.update(role.getUuid(), roleDto);

		verify(roleRepository, never()).save(any(Role.class));
	}

	@Test
	void shouldDeleteExistingRole() {
		when(roleRepository.existsByUuid(role.getUuid())).thenReturn(true);

		boolean isDeleted = roleService.delete(role.getUuid());

		assertTrue(isDeleted, "Role should be deleted when it exists for ID: " + role.getUuid());
		verify(roleRepository).deleteByUuid(role.getUuid());
	}

	@Test
	void shouldNotDeleteNonExistingRole() {
		when(roleRepository.existsByUuid(role.getUuid())).thenReturn(false);

		boolean isDeleted = roleService.delete(role.getUuid());

		assertFalse(isDeleted, "Role should not be deleted when it does not exist for ID: " + role.getUuid());
		verify(roleRepository, never()).deleteById(anyLong());
	}

	@Test
	void shouldGetAllRoles() {
		List<Role> roles = Collections.singletonList(role);
		when(roleRepository.findAll()).thenReturn(roles);

		List<RoleDto> foundRoles = roleService.getAllByUuid();

		assertEquals(1, foundRoles.size(), "The size of found roles does not match the expected count.");
		assertEquals(roleDto.getName(), foundRoles.get(0).getName(), "The found role name does not match.");
		verify(roleRepository).findAll();
	}
}
