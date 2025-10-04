package org.lessons.spring_library.repository;

import java.util.Optional;

import org.lessons.spring_library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByUsername(String username);
}
