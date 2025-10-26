package guru.qa.niffler.test.fake;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

@Disabled
@Order(1)
class UsersBeforeAllApiTest {

  @User
  @Test
  void emptyDbTest(UserDataJson user) {
    List<UserDataJson> allUsers = new UsersApiClient().allUsers(user.username(), null);
    Assertions.assertTrue(allUsers.isEmpty());
  }
}
