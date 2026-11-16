package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.enums.Role;
import com.server.mentorgrowth.enums.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
