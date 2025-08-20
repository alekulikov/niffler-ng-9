package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@Disabled
public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    assertThrowsExactly(RuntimeException.class, () -> spendDbClient.createSpend
        (
            new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                    null,
                    "cat-name-tx-2",
                    "duck",
                    false
                ),
                CurrencyValues.RUB,
                1000.0,
                "spend-name-tx",
                null
            )
        )
    );
  }

  @Test
  void userJdbcTest() {
    UsersDbClient userDbClient = new UsersDbClient();
    String username = randomUsername() + "-jdbc";

    UserDataJson user = userDbClient.createUser(
        new UserAuthJson(
            null,
            username,
            "12345",
            true,
            true,
            true,
            true
        ),
        new UserDataJson(
            null,
            username,
            CurrencyValues.EUR,
            "",
            "",
            "",
            null,
            null
        )
    );

    System.out.println(user);
  }

  @Test
  void userSpringJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    String username = randomUsername() + "-springJdbc";

    UserDataJson user = usersDbClient.createUserSpringJdbc(
        new UserAuthJson(
            null,
            username,
            "12345",
            true,
            true,
            true,
            true
        ),
        new UserDataJson(
            null,
            username,
            CurrencyValues.RUB,
            "",
            "",
            "",
            null,
            null
        )
    );
    System.out.println(user);
  }

  @Test
  void categoryJdbcTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryJson category = spendDbClient.createCategory(
        new CategoryJson(
            null,
            randomCategoryName() + "-jdbc",
            "duck",
            true
        )
    );
    System.out.println(category);
  }

  @Test
  void spendJdbcTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "Образование",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-jdbc",
            "duck"
        )
    );
    System.out.println(spend);
  }

  @Test
  void categorySpringJdbcTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    CategoryJson category = spendDbClient.createCategorySpringJdbc(
        new CategoryJson(
            null,
            randomCategoryName() + "-springJdbc",
            "duck",
            true
        )
    );
    System.out.println(category);
  }

  @Test
  void spendSpringJdbcTest() {
    SpendDbClient spendDbClient = new SpendDbClient();
    SpendJson spend = spendDbClient.createSpendSpringJdbc(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "Образование",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-springJdbc",
            "duck"
        )
    );
    System.out.println(spend);
  }

  /**
   * Используя ChainedTransactionManager не получится откатить внутреннюю транзакцию при сбое во внешней,
   * так как вызывается метод reverse() и commit() выполняется в обратном порядке, а двойное подтверждение в
   * этом механизме отсутствует. Это означает, что для внутренней транзакции уже может быть выполнен commit(), когда
   * во внешней будет выброшено исключение, и откатить внутреннюю уже не получится. Корректно откатить все изменения
   * получится только если сбой произойдет в самой последней транзакции. На случай ошибки предусмотрен выброс
   * исключения HeuristicCompletionException, которое может иметь состояние STATE_ROLLED_BACK (произошел откат всех
   * транзакций) и STATE_MIXED (произошла ошибка не в самой последней транзакции и данные не консистентны).
   **/
  @Test
  void userSpringJdbcChainedTrxTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserDataJson user = usersDbClient.createUserSpringJdbcChainedTrx(
        new UserDataJson(
            null,
            randomUsername() + "-springJdbcChainedTrx",
            CurrencyValues.RUB,
            null,
            null,
            null,
            null,
            null
        )
    );
    System.out.println(user);
  }

  @Test
  void userRepositoryJdbcTest() {
    UserdataUserRepository userRepository = new UserdataUserRepositoryJdbc();
    var users = userRepository.findAll();
    System.out.println(users.size());
    users.forEach(user -> System.out.println(user.getId() + " " + user.getUsername()));
  }

  @Test
  void friendshipRepositoryJdbcTest() {
    UserdataUserRepository userRepository = new UserdataUserRepositoryJdbc();
    var firstUser = userRepository.create(
        new UserdataUserEntity(randomUsername() + "-bestFriends", CurrencyValues.RUB));
    var secondUser = userRepository.create(
        new UserdataUserEntity(randomUsername() + "-bestFriends", CurrencyValues.RUB));
    userRepository.addFriend(firstUser, secondUser);
  }

  @Test
  void authRepositoryJdbcTest() {
    AuthUserRepository userRepository = new AuthUserRepositoryJdbc();
    var users = userRepository.findAll();
    System.out.println(users.size());
    users.forEach(user -> System.out.println(user.getId() + " " + user.getUsername()));
  }

  @Test
  void userRepositorySpringJdbcTest() {
    UserdataUserRepository userRepository = new UserdataUserRepositorySpringJdbc();
    var users = userRepository.findAll();
    System.out.println(users.size());
    users.forEach(user -> System.out.println(user.getId() + " " + user.getUsername()));
  }

  @Test
  void friendshipRepositorySpringJdbcTest() {
    UserdataUserRepository userRepository = new UserdataUserRepositorySpringJdbc();
    var firstUser = userRepository.create(
        new UserdataUserEntity(randomUsername() + "-bestFriends", CurrencyValues.RUB));
    var secondUser = userRepository.create(
        new UserdataUserEntity(randomUsername() + "-bestFriends", CurrencyValues.RUB));
    userRepository.addFriend(firstUser, secondUser);
  }

  @Test
  void authRepositorySpringJdbcTest() {
    AuthUserRepository userRepository = new AuthUserRepositorySpringJdbc();
    var users = userRepository.findAll();
    System.out.println(users.size());
    users.forEach(user -> System.out.println(user.getId() + " " + user.getUsername()));
  }
}
