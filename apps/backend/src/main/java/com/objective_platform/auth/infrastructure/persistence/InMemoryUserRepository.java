package com.objective_platform.auth.infrastructure.persistence;

import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.domain.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email)).map(User::deepClone);
    }

    @Override
    public void save(User user) {
        users.put(user.email(), user);
    }
}
