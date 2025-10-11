package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  private final EntityManager entityManager = em(CFG.userdataJdbcUrl());

  @Nonnull
  @Override
  public UserdataUserEntity create(UserdataUserEntity user) {
    entityManager.joinTransaction();
    entityManager.persist(user);
    return user;
  }

  @Nonnull
  @Override
  public UserdataUserEntity update(UserdataUserEntity user) {
    entityManager.joinTransaction();
    return entityManager.merge(user);
  }

  @Nonnull
  @Override
  public Optional<UserdataUserEntity> findById(UUID id) {
    return Optional.ofNullable(
        entityManager.find(UserdataUserEntity.class, id)
    );
  }

  @Nonnull
  @Override
  public Optional<UserdataUserEntity> findByUsername(String username) {
    try {
      return Optional.of(
          entityManager.createQuery("select u from UserdataUserEntity u where u.username =: username", UserdataUserEntity.class)
              .setParameter("username", username)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public void sendInvitation(UserdataUserEntity requester, UserdataUserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.PENDING, addressee);
  }

  @Override
  public void addFriend(UserdataUserEntity requester, UserdataUserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
  }

  @Override
  public void remove(UserdataUserEntity user) {
    UserdataUserEntity managed = entityManager.find(UserdataUserEntity.class, user.getId());
    if (managed != null) {
      entityManager.joinTransaction();
      entityManager.remove(managed);
    }
  }
}
