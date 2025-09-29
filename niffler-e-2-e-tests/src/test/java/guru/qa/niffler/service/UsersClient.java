package guru.qa.niffler.service;

import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

  static UsersClient getInstance() {
    return "api".equals(System.getProperty("client.impl"))
        ? new UsersApiClient()
        : new UsersDbClient();
  }

  @Nonnull
  UserDataJson createUser(String username, String password);

  void createIncomeInvitations(UserDataJson targetUser, int count);

  void createOutcomeInvitations(UserDataJson targetUser, int count);

  void createFriends(UserDataJson targetUser, int count);
}
