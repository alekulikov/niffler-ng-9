package guru.qa.niffler.test.web;

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
  void xaTxTest() {
    UsersDbClient userDbClient = new UsersDbClient();

    userDbClient.createUser(
        new UserAuthJson(
            null,
            randomUsername(),
            "12345",
            true,
            true,
            true,
            true
        ),
        new UserDataJson(
            null,
            "duck",
            CurrencyValues.EUR,
            "",
            "",
            "",
            null,
            null
        )
    );
  }

  @Test
  void springJdbcTest() {
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
}
