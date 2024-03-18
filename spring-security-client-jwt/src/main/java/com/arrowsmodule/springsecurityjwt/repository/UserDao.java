package com.arrowsmodule.springsecurityjwt.repository;

import com.arrowsmodule.springsecurityjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    public Optional<User> findByEmail(String email);
}
