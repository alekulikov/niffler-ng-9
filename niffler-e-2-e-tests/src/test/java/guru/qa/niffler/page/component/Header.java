package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent {

  private final SelenideElement mainPageLink = self.$(By.linkText("Niffler"));
  private final SelenideElement addSpendingBtn = self.$(By.linkText("New spending"));
  private final SelenideElement menuBtn = self.$("button[aria-label='Menu']");
  private final ElementsCollection menuItems = $("ul[role='menu']").$$("li");

  public Header() {
    super($("#root header"));
  }

  @Step("Go to Friends page")
  @Nonnull
  public FriendsPage goFriendsPage() {
    menuBtn.click();
    menuItems.find(text("Friends")).click();
    return new FriendsPage();
  }

  @Step("Go to All Peoples page")
  @Nonnull
  public AllPeoplePage goAllPeoplesPage() {
    menuBtn.click();
    menuItems.find(text("All People")).click();
    return new AllPeoplePage();
  }

  @Step("Go to Profile page")
  @Nonnull
  public ProfilePage goProfilePage() {
    menuBtn.click();
    menuItems.find(text("Profile")).click();
    return new ProfilePage();
  }

  @Step("Go add new spending page")
  @Nonnull
  public EditSpendingPage goAddSpendingPage() {
    addSpendingBtn.click();
    return new EditSpendingPage();
  }

  @Step("Go to main page")
  @Nonnull
  public MainPage goMainPage() {
    mainPageLink.click();
    return new MainPage();
  }
}
