package com.auth.domain.Roles.services;
import com.auth.domain.Roles.dto.RoleResponseDto;
import com.auth.domain.Roles.dto.RolesDto;
import com.auth.domain.Roles.dto.UpdateRoleDto;
import com.auth.domain.Roles.entity.Roles;
import com.auth.domain.Roles.mapper.RoleMapper;
import com.auth.domain.Roles.repository.RolesRepository;
import com.auth.globalExceptions.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements  IRolesService {
    private  final RolesRepository rolesRepository;
    private  final RoleMapper roleMapper;

    @Override
    public RoleResponseDto createRole(RolesDto rolesDto) {
             rolesRepository.findByRoleName(rolesDto.getRoleName()).ifPresent(roles -> {
                         throw new IllegalArgumentException(
                                 "Role already exists with name: " + rolesDto.getRoleName()
                         );
                     }
             );
             try {
                 Roles role = roleMapper.toEntity(rolesDto);
                 Roles saved = rolesRepository.save(role);
                 return roleMapper.toResponse(saved);
             } catch (DataIntegrityViolationException ex) {
                 throw new ConflictException("Database constraint violation: Role may already exist");
             }
    }

    @Override
    public RoleResponseDto updateRole(UpdateRoleDto updateRoleDto,String roleId) {

         try{
                rolesRepository.findById(roleId).orElseThrow(()->
                        new IllegalArgumentException("Roles Id can't not be null"));
                Roles roles = roleMapper.updateEntity(updateRoleDto);
                Roles save = rolesRepository.save(roles);
                return  roleMapper.toResponse(save);
         } catch (RuntimeException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public RoleResponseDto getRoles(String roleId) {

        Roles role = rolesRepository.findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException("Role not found with id: " + roleId)
                );
        return roleMapper.toResponse(role);
    }

    @Override
    public List<RoleResponseDto> getAllRoles(int page, int limit) {

        Pageable pageable = PageRequest.of(page, limit);

        List<Roles> roles = rolesRepository.findAll(pageable)
                .getContent();

        return roles.stream()
                .map(roleMapper::toResponse)
                .toList();
    }
}
