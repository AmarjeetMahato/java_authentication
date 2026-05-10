package com.auth.domain.Roles.mapper;

import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.dto.UpdateRoleDto;
import com.auth.domain.Roles.entity.Roles;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    // DTO → ENTITY
    public Roles toEntity(RolesDto dto){

         if(dto == null) {
           throw new   IllegalArgumentException("Role Dto must not be null");
         }
         return  Roles.builder()
                 .roleName(dto.getRoleName())
                 .description(dto.getDescription())
                 .build();
    }


    public void updateEntity(Roles role, UpdateRoleDto dto) {

        if (role == null) {
            throw new IllegalArgumentException("Role entity must not be null");
        }

        if (dto == null) {
            throw new IllegalArgumentException("UpdateRoleDto must not be null");
        }

        // Update only if not null (supports partial update)
        if (dto.getRoleName() != null) {
            role.setRoleName(dto.getRoleName());
        }

        if (dto.getDescription() != null) {
            role.setDescription(dto.getDescription());
        }
    }

    public RoleResponseDto toResponse(Roles roles){
            if(roles == null){
                 throw  new IllegalArgumentException("Roles must not be null");
            }

            return  RoleResponseDto
                    .builder()
                    .roleName(roles.getRoleName())
                    .description(roles.getDescription())
                    .createdAt(roles.getCreatedAt())
                    .updatedAt(roles.getUpdatedAt())
                    .build();
    }
}
