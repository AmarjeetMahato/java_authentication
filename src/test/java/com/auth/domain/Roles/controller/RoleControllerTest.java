package com.auth.domain.Roles.controller;

import com.auth.domain.Roles.controllers.RolesControllers;
import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.services.RolesServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static java.lang.reflect.Array.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RolesControllers.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RolesServiceImpl roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create role successfully")
    void should_create_role_successfully() throws Exception {

        RolesDto dto = RolesDto.builder()
                .roleName("ADMIN")
                .build();

        RoleResponseDto response = RoleResponseDto.builder()
                .id("1")
                .roleName("ADMIN")
                .build();

        when(roleService.createRole(any(RolesDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("ADMIN"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    @DisplayName("Should fetch by roleId successfully")
    void should_fetch_role_successfully() throws Exception {

        // Arrange
        String roleId = "1";

        RoleResponseDto response = RoleResponseDto.builder()
                .id(roleId)   // MUST be String
                .roleName("ADMIN")
                .build();

        // Mock service
        when(roleService.getRoles(roleId))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/v1/roles/{roleId}/get_role", roleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))   // force string match
                .andExpect(jsonPath("$.roleName").value("ADMIN"));

        // Verify
        verify(roleService).getRoles(roleId);
    }
}
