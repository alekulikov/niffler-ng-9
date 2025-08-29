package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.UsersClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  public UserDataJson createUser(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity authUser = authUserEntity(username, password);
          authUserRepository.create(authUser);
          return UserDataJson.fromEntity(
              userdataUserRepository.create(userEntity(username)),
              null
          );
        }
    );
  }

  @Override
  public void createIncomeInvitations(UserDataJson targetUser, int count) {
    if (count > 0) {
      UserdataUserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        targetUser.testData().incomeInvitations()
            .add(UserDataJson.fromEntity(xaTransactionTemplate.execute(() -> {
                  String username = randomUsername();
                  AuthUserEntity authUser = authUserEntity(username, "12345");
                  authUserRepository.create(authUser);
                  UserdataUserEntity requester = userdataUserRepository.create(userEntity(username));
                  userdataUserRepository.sendInvitation(requester, targetEntity);
                  return requester;
                }
            ), FriendshipStatus.INVITE_RECEIVED));
      }
    }
  }

  @Override
  public void createOutcomeInvitations(UserDataJson targetUser, int count) {
    if (count > 0) {
      UserdataUserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        targetUser.testData().outcomeInvitations()
            .add(UserDataJson.fromEntity(xaTransactionTemplate.execute(() -> {
                  String username = randomUsername();
                  AuthUserEntity authUser = authUserEntity(username, "12345");
                  authUserRepository.create(authUser);
                  UserdataUserEntity addressee = userdataUserRepository.create(userEntity(username));
                  userdataUserRepository.sendInvitation(targetEntity, addressee);
                  return addressee;
                }
            ), FriendshipStatus.INVITE_SENT));
      }
    }
  }

  @Override
  public void createFriends(UserDataJson targetUser, int count) {
    if (count > 0) {
      UserdataUserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        targetUser.testData().friends()
            .add(UserDataJson.fromEntity(xaTransactionTemplate.execute(() -> {
                  String username = randomUsername();
                  AuthUserEntity authFriend = authUserEntity(username, "12345");
                  authUserRepository.create(authFriend);
                  UserdataUserEntity userFriend = userdataUserRepository.create(userEntity(username));
                  userdataUserRepository.addFriend(targetEntity, userFriend);
                  return userFriend;
                }
            ), FriendshipStatus.FRIEND));
      }
    }
  }

  private UserdataUserEntity userEntity(String username) {
    UserdataUserEntity ue = new UserdataUserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(
        Arrays.stream(Authority.values()).map(
            e -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setUser(authUser);
              ae.setAuthority(e);
              return ae;
            }
        ).toList()
    );
    return authUser;
  }
}
