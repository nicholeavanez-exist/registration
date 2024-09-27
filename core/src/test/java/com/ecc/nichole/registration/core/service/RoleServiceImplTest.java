package com.ecc.nichole.registration.core.service;

import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.repo.RoleRepository;
import com.ecc.nichole.registration.core.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

	@InjectMocks
	private RoleServiceImpl roleService;

	private Role role;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		role = new Role();
	}

	@ParameterizedTest
	@ValueSource(strings = {"Admin", "Developer", "QA", "BA"})
	void shouldCreateRole(String roleName) {
		role.setName(roleName);

		when(roleRepository.save(any(Role.class))).thenReturn(role);
		roleService.create(role);

		assertEquals(roleName, role.getName(), "Created role name is not equal to the value source passed.");
		verify(roleRepository).save(role);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
	void shouldGetExistingRole(Long roleId) {
		role.setId(roleId);
		when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

		Optional<Role> retrievedRole = roleService.getById(roleId);

		verify(roleRepository).findById(roleId);
		assertTrue(retrievedRole.isPresent(), "Retrieved role should be present.");
		assertEquals(role.getId(), retrievedRole.get().getId(), "Retrieved role ID does not match.");
		assertEquals(role.getName(), retrievedRole.get().getName(), "Retrieved role name does not match.");
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L, 3L})
	void shouldNotGetNonExistingRole(Long roleId) {
		when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

		Optional<Role> foundRole = roleService.getById(roleId);

		assertFalse(foundRole.isPresent(), "Found role should not be present for ID: " + roleId);
		verify(roleRepository).findById(roleId);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L})
	void shouldUpdateExistingRole(Long roleId) {
		when(roleRepository.existsById(roleId)).thenReturn(true);
		when(roleRepository.save(any(Role.class))).thenReturn(role);

		roleService.update(roleId, role);

		verify(roleRepository).save(role);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L})
	void shouldNotUpdateNonExistingRole(Long roleId) {
		when(roleRepository.existsById(roleId)).thenReturn(false);

		roleService.update(roleId, role);

		verify(roleRepository, never()).save(any(Role.class));
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L})
	void shouldDeleteExistingRole(Long roleId) {
		when(roleRepository.existsById(roleId)).thenReturn(true);

		boolean isDeleted = roleService.delete(roleId);

		assertTrue(isDeleted, "Role should be deleted when it exists for ID: " + roleId);
		verify(roleRepository).deleteById(roleId);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L})
	void shouldNotDeleteNonExistingRole(Long roleId) {
		when(roleRepository.existsById(roleId)).thenReturn(false);

		boolean isDeleted = roleService.delete(roleId);

		assertFalse(isDeleted, "Role should not be deleted when it does not exist for ID: " + roleId);
		verify(roleRepository, never()).deleteById(anyLong());
	}

	@ParameterizedTest
	@ValueSource(ints = {1})
	void shouldGetAllRoles(int count) {
		List<Role> roles = Collections.singletonList(role);
		when(roleRepository.findAll()).thenReturn(roles);

		List<Role> foundRoles = roleService.getAllById();

		assertEquals(count, foundRoles.size(), "The size of found roles does not match the expected count.");
		assertEquals(role.getName(), foundRoles.get(0).getName(), "The found role name does not match.");
		verify(roleRepository).findAll();
	}
}
