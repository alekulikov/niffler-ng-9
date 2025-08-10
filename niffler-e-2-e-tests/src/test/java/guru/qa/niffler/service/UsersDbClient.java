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
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserAuthJson;
import guru.qa.niffler.model.UserDataJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
  private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
  private final UserdataUserDao userdataUserDaoSpringJdbc = new UserdataUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
  private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
  private final UserdataUserDao userdataUserDaoJdbc = new UserdataUserDaoJdbc();

  private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
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
}
