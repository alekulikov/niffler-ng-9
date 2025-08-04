package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.UserAuthJson;
import guru.qa.niffler.model.UserDataJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserDataJson createUser(UserAuthJson userAuth, UserDataJson userData) {
    return xaTransaction(
        TRANSACTION_READ_COMMITTED,
        new XaFunction<>(connection -> {
          AuthUserEntity userEntity = AuthUserEntity.fromJson(userAuth);
          userEntity.setPassword(pe.encode(userAuth.password()));
          AuthUserDao authUserDao = new AuthUserDaoJdbc(connection);
          authUserDao.create(userEntity);

          AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
              .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(userEntity);
                    ae.setAuthority(a);
                    return ae;
                  }
              ).toArray(AuthorityEntity[]::new);
          AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc(connection);
          authAuthorityDao.create(authorityEntities);

          return null;
        },
            CFG.authJdbcUrl()),
        new XaFunction<>(connection -> {
          UserDao userDao = new UserDaoJdbc(connection);
          return UserDataJson.fromEntity(
              userDao.createUser(guru.qa.niffler.data.entity.userdata.UserEntity.fromJson(userData))
          );
        },
            CFG.userdataJdbcUrl())
    );
  }

  public UserDataJson createUserSpringJdbc(UserAuthJson userAuth, UserDataJson userData) {
    AuthUserEntity userEntity = AuthUserEntity.fromJson(userAuth);
    userEntity.setPassword(pe.encode(userAuth.password()));
    AuthUserDao authUserDao = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()));
    authUserDao.create(userEntity);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
        .map(a -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setUser(userEntity);
              ae.setAuthority(a);
              return ae;
            }
        ).toArray(AuthorityEntity[]::new);

    new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
        .create(authorityEntities);

    return UserDataJson.fromEntity(
        new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
            .create(guru.qa.niffler.data.entity.userdata.UserEntity.fromJson(userData))
    );
  }
}
