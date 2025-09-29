package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

  private final SelenideElement friendsTab = $(By.linkText("Friends"));
  private final ElementsCollection people = $$("#all tr");
  private final SearchField peopleSearch = new SearchField();

  @Step("Open AllPeople tab")
  @Nonnull
  public FriendsPage selectFriendsTab() {
    friendsTab.click();
    return new FriendsPage();
  }

  @Step("Check that user has outcome requests to: '{usernames}'")
  @Nonnull
  public AllPeoplePage checkThatOutcomeRequestsContains(String... usernames) {
    for (String username : usernames) {
      search(username);
      people.find(text(username))
          .shouldBe(visible)
          .shouldHave(text("Waiting..."));
    }
    return this;
  }

  @Step("Search user with name '{username}'")
  @Nonnull
  public AllPeoplePage search(String username) {
    peopleSearch.search(username);
    return this;
  }

  @Step("Check that all people page been load")
  @Nonnull
  @Override
  public AllPeoplePage checkThatPageLoaded() {
    friendsTab.shouldBe(visible);
    return this;
  }
}
