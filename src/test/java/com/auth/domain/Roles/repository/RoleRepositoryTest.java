package com.auth.domain.Roles.repository;


import com.auth.domain.Roles.entity.Roles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private  RolesRepository rolesRepository;

    @Test
    @DisplayName("Should successfully save a role and retrieve it by its name from the database")
    void  should_Save_RoleAnd_Find_Role(){
        Roles roles = Roles.builder()
                           .roleName("ADMIN")
                           .build();
        rolesRepository.save(roles);
        Optional<Roles> role =  rolesRepository.findByRoleName(roles.getRoleName());
        assertTrue(role.isPresent());
    }

    @Test
    @DisplayName("Should successfully fetch the role with give roleId")
    void should_FindRole_By_Id(){
        Roles roles = Roles.builder()
                .roleName("ADMIN")
                .build();

        Roles save = rolesRepository.save(roles);

        Optional<Roles> role = rolesRepository.findById(save.getId());
        assertTrue(role.isPresent());
    }

    @Test
    @DisplayName("Should successfully update the role with given role Id")
    void should_update_role_by_ID(){

        // ✅ Step 1: Save initial role
        Roles roles = Roles.builder()
                .roleName("ADMIN")
                .build();

        Roles saved = rolesRepository.save(roles);

        // ✅ Step 2: Fetch existing entity
        Roles existing = rolesRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // ✅ Step 3: Update field
        existing.setRoleName("USER");

        // ✅ Step 4: Save again (this is UPDATE, not INSERT)
        rolesRepository.save(existing);

        // ✅ Step 5: Verify
        Roles updated = rolesRepository.findById(saved.getId())
                .orElseThrow();

        assertTrue(updated.getRoleName().equals("USER"));
    }

    @Test
    @DisplayName("Should return paginated roles from database")
    void should_return_paginated_roles() {

        // Arrange → save multiple roles
        rolesRepository.save(Roles.builder().roleName("ADMIN").build());
        rolesRepository.save(Roles.builder().roleName("USER").build());
        rolesRepository.save(Roles.builder().roleName("MANAGER").build());

        Pageable pageable = PageRequest.of(0, 2);

        // Act
        Page<Roles> result = rolesRepository.findAll(pageable);

        // Assert
        assertEquals(2, result.getContent().size()); // limit = 2
        assertTrue(result.getTotalElements() >= 3);
    }

    @Test
    @DisplayName("Should delete role from database successfully")
    void should_delete_role_successfully() {

        // Arrange → save role first
        Roles role = Roles.builder()
                .roleName("ADMIN")
                .build();

        Roles saved = rolesRepository.save(role);

        // Act → delete
        rolesRepository.delete(saved);

        // Assert → verify deleted
        Optional<Roles> deletedRole =
                rolesRepository.findById(saved.getId());

        assertTrue(deletedRole.isEmpty());
    }



}
