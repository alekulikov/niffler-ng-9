package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.jdbc.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.jdbc.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.springjdbc.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.jdbc.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.springjdbc.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.springjdbc.SpendRepositorySpringJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class JdbcTest {

  @Test
  void userRepositoryJdbcTest() {
    UserdataUserRepository userRepository = new UserdataUserRepositoryJdbc();
    var user = userRepository.findByUsername("duck").get();
    System.out.println(user.getId() + " " + user.getUsername());
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
    var user = userRepository.findByUsername("duck").get();
    System.out.println(user.getId() + " " + user.getUsername());
  }

  @Test
  void userRepositorySpringJdbcTest() {
    UserdataUserRepository userRepository = new UserdataUserRepositorySpringJdbc();
    var user = userRepository.findByUsername("duck").get();
    System.out.println(user.getId() + " " + user.getUsername());
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
    var user = userRepository.findByUsername("duck").get();
    System.out.println(user.getId() + " " + user.getUsername());
  }

  @Test
  void spendRepositoryJdbcTest() {
    SpendRepository spendRepository = new SpendRepositoryJdbc();
    var category = spendRepository.findCategoryByUsernameAndCategoryName("duck", "Образование").get();
    System.out.println(category.getId() + " " + category.getName());
  }

  @Test
  void spendRepositorySpringJdbcTest() {
    SpendRepository spendRepository = new SpendRepositorySpringJdbc();
    var category = spendRepository.findCategoryByUsernameAndCategoryName("duck", "Образование").get();
    System.out.println(category.getId() + " " + category.getName());
  }

  @Test
  void createUserDbClientTrxTest() {
    UsersClient usersClient = new UsersDbClient();
    var user = usersClient.createUser(randomUsername(), "12345");
    usersClient.createFriends(user, 3);
    System.out.println(user.username());
  }

  @Test
  void createSpendDbClientTrxTest() {
    SpendClient spendClient = new SpendDbClient();
    var spend = new SpendJson(
        null,
        new Date(),
        new CategoryJson(null, randomCategoryName(), "duck", true),
        CurrencyValues.EUR,
        1000.0,
        "description",
        "duck"
    );
    spend = spendClient.createSpend(spend);
    System.out.println(spend.id() + " " + spend.description());
  }
}
