package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {

  UserdataUserEntity create(UserdataUserEntity user);

  UserdataUserEntity update(UserdataUserEntity user);

  Optional<UserdataUserEntity> findById(UUID id);

  Optional<UserdataUserEntity> findByUsername(String username);

  void remove(UserdataUserEntity user);

  void sendInvitation(UserdataUserEntity requester, UserdataUserEntity addressee);

  void addFriend(UserdataUserEntity requester, UserdataUserEntity addressee);
}
