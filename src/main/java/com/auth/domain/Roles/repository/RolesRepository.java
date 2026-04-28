package com.auth.domain.Roles.repository;

import com.auth.domain.Roles.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles,String> {

    Optional<Roles> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

    List<Roles> findByRoleNameIn(Set<String> roleNames);
}
