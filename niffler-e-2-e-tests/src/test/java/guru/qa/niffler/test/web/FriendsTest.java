package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

@WebTest
public class FriendsTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @User(
      friends = 1
  )
  void friendShouldBePresentInFriendsTable(UserDataJson user) {
    final UserDataJson friend = user.testData().friends().getFirst();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .goFriendsPage()
        .checkThatFriendsContains(friend.username());
  }

  @Test
  void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.password())
        .goFriendsPage()
        .checkThatFriendsEmpty();
  }

  @Test
  @User(
      incomeInvitations = 1
  )
  void incomeInvitationBePresentInFriendsTable(UserDataJson user) {
    final UserDataJson income = user.testData().incomeInvitations().getFirst();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .goFriendsPage()
        .checkThatIncomeRequestsContains(income.username());
  }

  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.password())
        .goFriendsPage()
        .selectAllPeopleTab()
        .checkThatOutcomeRequestsContains(user.outcome());
  }
}
