package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage {

  private final ElementsCollection incomeRequests = $$("#requests tr");
  private final ElementsCollection friends = $$("#friends tr");
  private final SelenideElement allPeopleTab = $(By.linkText("All people"));
  private final SelenideElement dialogWindow = $("div[role='dialog']");
  private final SearchField peopleSearch = new SearchField();

  @Step("Accept invitation from {username}")
  @Nonnull
  public FriendsPage acceptFriendInvitationFromUser(String username) {
    incomeRequests.find(text(username))
        .$(byText("Accept")).click();
    return this;
  }

  @Step("Decline invitation from {username}")
  @Nonnull
  public FriendsPage declineFriendInvitationFromUser(String username) {
    incomeRequests.find(text(username))
        .$(byText("Decline")).click();
    dialogWindow.$(byText("Decline")).click();
    return this;
  }

  @Step("Check that user has incoming requests from: '{usernames}'")
  @Nonnull
  public FriendsPage checkThatIncomeRequestsContains(String... usernames) {
    for (String username : usernames) {
      search(username);
      incomeRequests.find(text(username)).shouldBe(visible);
    }
    return this;
  }

  @Step("Check that user have friends: '{usernames}'")
  @Nonnull
  public FriendsPage checkThatFriendsContains(String... usernames) {
    for (String username : usernames) {
      search(username);
      friends.find(text(username)).shouldBe(visible);
    }
    return this;
  }

  @Step("Check that user has no friends")
  @Nonnull
  public FriendsPage checkThatFriendsEmpty() {
    friends.shouldBe(empty);
    return this;
  }

  @Step("Check that user has no invitations")
  @Nonnull
  public FriendsPage checkThatInvitationsEmpty() {
    incomeRequests.shouldBe(empty);
    return this;
  }

  @Step("Open AllPeople tab")
  @Nonnull
  public AllPeoplePage selectAllPeopleTab() {
    allPeopleTab.click();
    return new AllPeoplePage();
  }

  @Step("Search user with name '{username}'")
  @Nonnull
  public FriendsPage search(String username) {
    peopleSearch.search(username);
    return this;
  }
}
