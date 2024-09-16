package io.github.justinscottjenecke.spring_security_jpa_tutorial.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
