package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserAuthJson;
import guru.qa.niffler.model.UserDataJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
  private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
  private final UserdataUserDao userdataUserDaoSpringJdbc = new UserdataUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
  private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
  private final UserdataUserDao userdataUserDaoJdbc = new UserdataUserDaoJdbc();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

  private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

  private final TransactionTemplate chainedTrxSpring = new TransactionTemplate(
      new ChainedTransactionManager(
          new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),
          new JdbcTransactionManager(DataSources.dataSource(CFG.userdataJdbcUrl()))
      )
  );

  public UserDataJson createUser(UserAuthJson userAuth, UserDataJson userData) {
    return xaTxTemplate.execute(() -> {

          AuthUserEntity userEntity = AuthUserEntity.fromJson(userAuth);
          userEntity.setPassword(pe.encode(userAuth.password()));
          authUserDaoJdbc.create(userEntity);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
              .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(userEntity);
                    ae.setAuthority(a);
                    return ae;
                  }
              ).toArray(AuthorityEntity[]::new);
          authAuthorityDaoJdbc.create(authorityEntities);

          return UserDataJson.fromEntity(
              userdataUserDaoJdbc.create(UserdataUserEntity.fromJson(userData))
          );
        }
    );
  }

  public UserDataJson createUserSpringJdbc(UserAuthJson userAuth, UserDataJson userData) {
    return xaTxTemplate.execute(() -> {
          AuthUserEntity userEntity = AuthUserEntity.fromJson(userAuth);
          userEntity.setPassword(pe.encode(userAuth.password()));
          authUserDaoSpringJdbc.create(userEntity);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
              .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(userEntity);
                    ae.setAuthority(a);
                    return ae;
                  }
              ).toArray(AuthorityEntity[]::new);
          authAuthorityDaoSpringJdbc.create(authorityEntities);

          return UserDataJson.fromEntity(
              userdataUserDaoSpringJdbc.create(UserdataUserEntity.fromJson(userData))
          );
        }
    );
  }

  public UserDataJson createUserSpringJdbcChainedTrx(UserDataJson user) {
    return chainedTrxSpring.execute(status -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);

          AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
              e -> {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setUser(createdAuthUser);
                ae.setAuthority(e);
                return ae;
              }
          ).toArray(AuthorityEntity[]::new);

          authAuthorityDaoSpringJdbc.create(authorityEntities);

          return UserDataJson.fromEntity(
              userdataUserDaoSpringJdbc.create(UserdataUserEntity.fromJson(user)));
        }
    );
  }

  public UserDataJson createUser(UserDataJson user) {
    return xaTxTemplate.execute(() -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
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
          authUserRepository.create(authUser);
          return UserDataJson.fromEntity(
              userdataUserDaoSpringJdbc.create(UserdataUserEntity.fromJson(user))
          );
        }
    );
  }

  public UserDataJson createUser(String username, String password) {
    return xaTxTemplate.execute(() -> {
          AuthUserEntity authUser = authUserEntity(username, password);
          authUserRepository.create(authUser);
          return UserDataJson.fromEntity(
              userdataUserRepository.create(userEntity(username))
          );
        }
    );
  }

  public void addIncomeInvitation(UserDataJson targetUser, int count) {
    if (count > 0) {
      UserdataUserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTxTemplate.execute(() -> {
              String username = randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserdataUserEntity adressee = userdataUserRepository.create(userEntity(username));
              userdataUserRepository.addIncomeInvitation(targetEntity, adressee);
              return null;
            }
        );
      }
    }
  }

  public void addOutcomeInvitation(UserDataJson targetUser, int count) {
    if (count > 0) {
      UserdataUserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTxTemplate.execute(() -> {
              String username = randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserdataUserEntity adressee = userdataUserRepository.create(userEntity(username));
              userdataUserRepository.addOutcomeInvitation(targetEntity, adressee);
              return null;
            }
        );
      }
    }
  }

  void addFriend(UserDataJson targetUser, int count) {

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
