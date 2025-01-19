package com.adsync.auth.application.ports;

import com.adsync.auth.domain.models.User;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findByEmail(String email);

  void save(User user);

  void clear();
}
