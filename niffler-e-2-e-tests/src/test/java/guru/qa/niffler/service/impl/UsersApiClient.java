package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.apache.commons.lang3.time.StopWatch;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class UsersApiClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final long MAX_WAIT_TIME = 2000L;

  private final AuthApi authApi;
  private final UserdataApi userdataApi;

  public UsersApiClient() {
    authApi = new RestClient.DefaultRestClient(CFG.authUrl()).create(AuthApi.class);
    userdataApi = new RestClient.DefaultRestClient(CFG.userdataUrl()).create(UserdataApi.class);
  }

  @Override
  @Nonnull
  @Step("Create user with name {username} using REST API")
  public UserDataJson createUser(String username, String password) {
    execute(authApi.getRegisterPage());
    execute(authApi.registerUser(username, password, password,
        ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")));

    StopWatch sw = StopWatch.createStarted();
    while (sw.getTime(TimeUnit.MILLISECONDS) < MAX_WAIT_TIME) {
      UserDataJson userJson = execute(userdataApi.currentUser(username)).body();
      if (userJson != null && userJson.id() != null) {
        return userJson.withPassword(password);
      }
      try {
        Thread.sleep(500L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    throw new RuntimeException("User with name " + username + " not created");
  }

  @Override
  @Step("Create {count} income invitations using REST API")
  public void createIncomeInvitations(UserDataJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        UserDataJson requester = createUser(randomUsername(), "12345");
        execute(userdataApi.sendInvitation(requester.username(), targetUser.username()));
        targetUser.testData().incomeInvitations().add(requester);
      }
    }
  }

  @Override
  @Step("Create {count} outcome invitations using REST API")
  public void createOutcomeInvitations(UserDataJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        UserDataJson addressee = createUser(randomUsername(), "12345");
        execute(userdataApi.sendInvitation(targetUser.username(), addressee.username()));
        targetUser.testData().outcomeInvitations().add(addressee);
      }
    }
  }

  @Override
  @Step("Create {count} friends using REST API")
  public void createFriends(UserDataJson targetUser, int count) {
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        UserDataJson userFriend = createUser(randomUsername(), "12345");
        execute(userdataApi.sendInvitation(userFriend.username(), targetUser.username()));
        execute(userdataApi.acceptInvitation(targetUser.username(), userFriend.username()));
        targetUser.testData().friends().add(userFriend);
      }
    }
  }

  @Step("Get all users using REST API")
  @Nonnull
  public List<UserDataJson> allUsers(String username, @Nullable String searchQuery) {
    List<UserDataJson> users = execute(userdataApi.allUsers(username, searchQuery)).body();
    return users != null ? users : Collections.emptyList();
  }

  @Nonnull
  protected <T> Response<T> execute(Call<T> request) {
    Response<T> response;
    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertTrue(response.isSuccessful());
    return response;
  }

}