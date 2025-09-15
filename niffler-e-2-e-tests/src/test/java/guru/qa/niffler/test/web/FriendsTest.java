package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
class FriendsTest {

  private static final Config CFG = Config.getInstance();

  @User(
      friends = 3
  )
  @Test
  void friendShouldBePresentInFriendsTable(UserDataJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goFriendsPage()
        .checkThatFriendsContains(user.testData().friends()
            .stream()
            .map(UserDataJson::username)
            .toArray(String[]::new)
        );
  }

  @User
  @Test
  void friendsTableShouldBeEmptyForNewUser(UserDataJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goFriendsPage()
        .checkThatFriendsEmpty();
  }

  @User(
      incomeInvitations = 2
  )
  @Test
  void incomeInvitationBePresentInFriendsTable(UserDataJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goFriendsPage()
        .checkThatIncomeRequestsContains(user.testData().incomeInvitations()
            .stream()
            .map(UserDataJson::username)
            .toArray(String[]::new)
        );
  }

  @User(
      outcomeInvitations = 1
  )
  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(UserDataJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goAllPeoplesPage()
        .checkThatOutcomeRequestsContains(user.testData().outcomeInvitations()
            .stream()
            .map(UserDataJson::username)
            .toArray(String[]::new)
        );
  }

  @User(incomeInvitations = 1)
  @Test
  void incomeInvitationMayBeAccepted(UserDataJson user) {
    final String userToAccept = user.testData().incomeInvitations().getFirst().username();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .checkMainPageBeenLoad()
        .getHeader()
        .goFriendsPage()
        .acceptFriendInvitationFromUser(userToAccept)
        .checkThatInvitationsEmpty()
        .checkThatFriendsContains(userToAccept);
  }

  @User(incomeInvitations = 1)
  @Test
  void incomeInvitationMayBeDeclined(UserDataJson user) {
    final String userToDecline = user.testData().incomeInvitations().getFirst().username();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .checkMainPageBeenLoad()
        .getHeader()
        .goFriendsPage()
        .declineFriendInvitationFromUser(userToDecline)
        .checkThatInvitationsEmpty()
        .checkThatFriendsEmpty();
  }
}
