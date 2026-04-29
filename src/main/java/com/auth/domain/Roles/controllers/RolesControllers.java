package com.auth.domain.Roles.controllers;

import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.dto.UpdateRoleDto;
import com.auth.domain.Roles.services.IRolesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolesControllers {

    private  final IRolesService rolesService;

    @PostMapping("/create")
    public ResponseEntity<RoleResponseDto> crateRoles(@Valid @RequestBody RolesDto rolesDto){
              RoleResponseDto responseDto = rolesService.createRole(rolesDto);
              return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{roleId}/update")
    public  ResponseEntity<RoleResponseDto> upateRole(
            @Valid
            @RequestBody UpdateRoleDto updateRoleDto,
            @PathVariable String roleId){
           RoleResponseDto responseDto = rolesService.updateRole(updateRoleDto,roleId);
           return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{roleId}/get_role")
    public ResponseEntity<RoleResponseDto> fetchRoleById(@PathVariable String roleId){
        RoleResponseDto responseDto = rolesService.getRoles(roleId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/get_all_roles")
    public ResponseEntity<List<RoleResponseDto>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<RoleResponseDto> response = rolesService.getAllRoles(page, limit);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roleId}/delete")
    public  ResponseEntity<?> deleteRole(@PathVariable String roleId){
          rolesService.deleteRole(roleId);
          return ResponseEntity.noContent().build();
    }
}
