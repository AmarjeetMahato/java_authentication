package com.auth.domain.Roles.controllers;

import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.services.IRolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolesControllers {

    private  final IRolesService rolesService;

    @PostMapping("/create")
    public ResponseEntity<RoleResponseDto> crateRoles(@RequestBody RolesDto rolesDto){
              RoleResponseDto responseDto = rolesService.createRole(rolesDto);
              return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
