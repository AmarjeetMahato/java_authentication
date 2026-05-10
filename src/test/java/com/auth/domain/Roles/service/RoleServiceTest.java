package com.auth.domain.Roles.service;

import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.dto.UpdateRoleDto;
import com.auth.domain.Roles.entity.Roles;
import com.auth.domain.Roles.mapper.RoleMapper;
import com.auth.domain.Roles.repository.RolesRepository;
import com.auth.domain.Roles.services.RolesServiceImpl;
import com.auth.globalExceptions.ConflictException;
import com.auth.globalExceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class RoleServiceTest {

    @InjectMocks
    private RolesServiceImpl roleService;  //real service with mocks injected

    @Mock
    private RolesRepository rolesRepository;  //fake dependency

    @Mock
    private RoleMapper roleMapper;   //enables Mockito

    // DTO → mapper → entity → save → mapper → response DTO
    @Test
    @DisplayName("Should create role successfully")
    void should_create_role_successfully() {

        // Arrange
//        pass data through DTO
        RolesDto dto = RolesDto.builder()
                .roleName("ADMIN")
                .build();
//    Here save in the Database and return it
        Roles savedRole = Roles.builder()
                .id("1")
                .roleName("ADMIN")
                .build();
//       this is response back to User
        RoleResponseDto responseDto = RoleResponseDto.builder()
                .id("1")
                .roleName("ADMIN")
                .build();

        // 1. role does not exist
        when(rolesRepository.findByRoleName("ADMIN"))
                .thenReturn(Optional.empty());

        // 2. save simulation (like DB behavior)
//        3. MOCK: SAVE BEHAVIOR (DB simulation)
        when(rolesRepository.save(Mockito.any(Roles.class)))
                .thenAnswer(invocation -> {
                    Roles role = invocation.getArgument(0);
                    role.setId("1");
                    return role;
                });

        // 3. mapper
        when(roleMapper.toEntity(Mockito.any(RolesDto.class)))
                .thenReturn(
                        Roles.builder()
                                .roleName("ADMIN")
                                .build()
                );

        when(roleMapper.toResponse(Mockito.any(Roles.class)))
                .thenReturn(
                        RoleResponseDto.builder()
                                .id("1")
                                .roleName("ADMIN")
                                .build()
                );

        // Act
        RoleResponseDto result = roleService.createRole(dto);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getRoleName());
        assertEquals("1", result.getId());

        // Verify interactions
        verify(rolesRepository).findByRoleName("ADMIN");
        verify(rolesRepository).save(Mockito.any(Roles.class));
        verify(roleMapper).toResponse(Mockito.any(Roles.class));
    }

    // ID → Find → Entity → Response
    @Test
    @DisplayName("Should fetch the Role by Id")
    void should_fetch_role_by_ID() {

        String roleId = "1";

        Roles role =   Roles.builder()
                            .id(roleId)
                            .roleName("Admin")
                            .build();

        RoleResponseDto responseDto = RoleResponseDto.builder()
                                                     .id(roleId)
                                                      .roleName("Admin")
                                                     .build();

        // 1. Mock repository
        when(rolesRepository.findById(roleId))
                .thenReturn(Optional.of(role));
        // 1. Mock Mapper
        when(roleMapper.toResponse(role))
                .thenReturn(responseDto);
        // Act
       RoleResponseDto result = roleService.getRoles(roleId);
        // Assert
       assertNotNull(result);
       assertEquals("Admin", result.getRoleName());
       assertEquals("1", result.getId());

//       Verify
        verify(rolesRepository).findById(roleId);
        verify(roleMapper).toResponse(role);
    }

    @Test
    @DisplayName("Should update the role with roleId successfully")
    void should_update_role_by_Id() {

        // Arrange
        String roleId = "1";

        UpdateRoleDto dto = UpdateRoleDto.builder()
                .roleName("USER")
                .description("Updated role")
                .build();

        // Existing role in DB
        Roles existingRole = Roles.builder()
                .id(roleId)
                .roleName("ADMIN")
                .description("Old role")
                .build();

        // Updated role (after save)
        Roles updatedRole = Roles.builder()
                .id(roleId)
                .roleName("USER")
                .description("Updated role")
                .build();

        RoleResponseDto responseDto = RoleResponseDto.builder()
                .id(roleId)
                .roleName("USER")
                .build();

        // 1. Mock findById
        when(rolesRepository.findById(roleId))
                .thenReturn(Optional.of(existingRole));

        // 2. Mock save (must return Roles)
        when(rolesRepository.save(Mockito.any(Roles.class)))
                .thenReturn(updatedRole);

        // 3. Mock mapper
        when(roleMapper.toResponse(updatedRole))
                .thenReturn(responseDto);

        // Act
        RoleResponseDto result = roleService.updateRole(dto, roleId);

        // Assert
        assertNotNull(result);
        assertEquals("USER", result.getRoleName());
        assertEquals(roleId, result.getId());

        // Verify
        verify(rolesRepository).findById(roleId);
        verify(rolesRepository).save(Mockito.any(Roles.class));
        verify(roleMapper).toResponse(updatedRole);
    }

    @Test
    @DisplayName("Should delete the role with roleId successfully")
    void should_delete_role_by_Id() {

        // Arrange
        String roleId = "1";

        Roles existingRole = Roles.builder()
                .id(roleId)
                .roleName("ADMIN")
                .build();

        // 1. Mock findById
        when(rolesRepository.findById(roleId))
                .thenReturn(Optional.of(existingRole));

        // Act
        roleService.deleteRole(roleId);

        // Assert (void method → verify behavior)
        verify(rolesRepository).findById(roleId);
        verify(rolesRepository).delete(existingRole);
    }

    @Test
    @DisplayName("Should throw exception when role not found")
    void  should_throw_exception_when_role_not_found(){

        String roleId = "1";
          when(rolesRepository.findById(roleId))
                  .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> roleService.getRoles(roleId)
                );

        verify(rolesRepository).findById(roleId);

    }

    @Test
    @DisplayName("Should throw exception when role already exists")
    void should_throw_exception_when_role_already_exists() {

        // Arrange
        RolesDto dto = RolesDto.builder()
                .roleName("ADMIN")
                .build();

        Roles existingRole = Roles.builder()
                .id("1")
                .roleName("ADMIN")
                .build();

        // Mock → role already exists
        when(rolesRepository.findByRoleName("ADMIN"))
                .thenReturn(Optional.of(existingRole));

        // Act + Assert
        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> roleService.createRole(dto)
        );

        // Optional: check message
        assertEquals(
                "Role already exists with name: ADMIN",
                ex.getMessage()
        );

        // Verify
        verify(rolesRepository).findByRoleName("ADMIN");

        // VERY IMPORTANT → save should NOT be called
        verify(rolesRepository, never()).save(Mockito.any());
    }
}