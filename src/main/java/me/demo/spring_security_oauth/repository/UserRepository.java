package me.demo.spring_security_oauth.repository;

import me.demo.spring_security_oauth.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);
}
