package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPage extends BasePage<MainPage> {

  @Getter
  private final Header header = new Header();
  @Getter
  private final SpendingTable spendingTable = new SpendingTable();
  @Getter
  protected final StatComponent statComponent = new StatComponent();

  @Step("Check that the page is loaded")
  @Nonnull
  @Override
  public MainPage checkThatPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler"));
    statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
    return this;
  }
}
