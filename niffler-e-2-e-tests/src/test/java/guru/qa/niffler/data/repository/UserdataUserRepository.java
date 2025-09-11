package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

  @Nonnull
  UserdataUserEntity create(UserdataUserEntity user);

  @Nonnull
  UserdataUserEntity update(UserdataUserEntity user);

  @Nonnull
  Optional<UserdataUserEntity> findById(UUID id);

  @Nonnull
  Optional<UserdataUserEntity> findByUsername(String username);

  void remove(UserdataUserEntity user);

  void sendInvitation(UserdataUserEntity requester, UserdataUserEntity addressee);

  void addFriend(UserdataUserEntity requester, UserdataUserEntity addressee);
}
