package guru.qa.niffler.test.fake;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

@Order(Integer.MAX_VALUE)
class UsersAfterAllApiTest {

  @User
  @Test
  void notEmptyDbTest(UserDataJson user) {
    List<UserDataJson> allUsers = new UsersApiClient().allUsers(user.username(), null);
    Assertions.assertFalse(allUsers.isEmpty());
  }
}

