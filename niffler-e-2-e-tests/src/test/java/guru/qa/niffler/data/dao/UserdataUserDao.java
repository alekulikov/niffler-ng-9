package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserDao {

  @Nonnull
  UserdataUserEntity create(UserdataUserEntity user);

  @Nonnull
  Optional<UserdataUserEntity> findById(UUID id);

  @Nonnull
  Optional<UserdataUserEntity> findByUsername(String username);

  void delete(UserdataUserEntity user);

  @Nonnull
  List<UserdataUserEntity> findAll();
}
