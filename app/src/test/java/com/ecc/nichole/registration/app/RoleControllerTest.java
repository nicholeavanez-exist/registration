package com.ecc.nichole.registration.app;

import com.ecc.nichole.registration.core.controller.RoleController;
import com.ecc.nichole.registration.core.model.Role;
import com.ecc.nichole.registration.core.service.impl.RoleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
@Import(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleServiceImpl roleServiceImpl; // Mocking the service

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRole() throws Exception {
        // Arrange
        Role newRole = new Role();
        newRole.setId(1L);
        newRole.setName("Admin");

        // Mock the service to perform create operation
        doNothing().when(roleServiceImpl).create(newRole);

        // Act & Assert
        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully saved role Admin with ID 1."));
    }

    @Test
    void testGetRole() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        // Mock the service to return the role
        when(roleServiceImpl.getById(1L)).thenReturn(Optional.of(role));

        // Act & Assert
        mockMvc.perform(get("/api/v1/role/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Optional.of(role))));
    }

    @Test
    void testGetRole_NotFound() throws Exception {
        // Arrange
        when(roleServiceImpl.getById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/role/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Cannot retrieve non-existing role with ID 2."));
    }

    @Test
    void testUpdateRole() throws Exception {
        // Arrange
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("Admin");

        Role updatedRole = new Role();
        updatedRole.setName("Super Admin");

        when(roleServiceImpl.getById(1L)).thenReturn(Optional.of(existingRole));

        // Act & Assert
        mockMvc.perform(put("/api/v1/role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated role Super Admin with ID 1."));
    }

    @Test
    void testUpdateRole_NotFound() throws Exception {
        // Arrange
        Role updatedRole = new Role();
        updatedRole.setName("Super Admin");

        when(roleServiceImpl.getById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/v1/role/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Cannot update non-existing role with ID 2."));
    }

    @Test
    void testDeleteRole() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        when(roleServiceImpl.getById(1L)).thenReturn(Optional.of(role));
        when(roleServiceImpl.delete(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/role/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted role Admin with ID 1."));
    }

    @Test
    void testDeleteRole_NotFound() throws Exception {
        // Arrange
        when(roleServiceImpl.getById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/role/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Cannot delete non-existing role with ID 2."));
    }

    @Configuration
    static class TestConfig {

    }
}
