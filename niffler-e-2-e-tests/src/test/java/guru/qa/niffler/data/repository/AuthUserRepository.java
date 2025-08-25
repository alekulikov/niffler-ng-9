package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

  AuthUserEntity create(AuthUserEntity user);

  AuthUserEntity update(AuthUserEntity user);

  Optional<AuthUserEntity> findByUsername(String username);

  Optional<AuthUserEntity> findById(UUID id);

  void remove(AuthUserEntity user);
}
