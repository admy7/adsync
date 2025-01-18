package com.objective_platform.auth.application.ports;

import com.objective_platform.auth.domain.models.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    void save(User user);

    void clear();
}
