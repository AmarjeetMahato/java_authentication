package com.auth.domain.Users.repository;

import com.auth.domain.Users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepsoitory  extends JpaRepository<User,String> {
}
