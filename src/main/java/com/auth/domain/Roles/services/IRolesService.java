package com.auth.domain.Roles.services;

import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.dto.UpdateRoleDto;

import java.util.List;

public interface IRolesService {

  RoleResponseDto createRole(RolesDto rolesDto);

  RoleResponseDto updateRole(UpdateRoleDto updateRoleDto,String roleId);

  RoleResponseDto getRoles(String roleId);

  List<RoleResponseDto> getAllRoles(int limit,int page);

}
