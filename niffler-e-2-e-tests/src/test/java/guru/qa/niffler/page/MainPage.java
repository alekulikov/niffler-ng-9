package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

  private final SelenideElement statistics = $("#stat");
  @Getter
  private final Header header = new Header();
  @Getter
  private final SpendingTable spendingTable = new SpendingTable();

  @Step("Check that main page been load")
  @Nonnull
  public MainPage checkMainPageBeenLoad() {
    spendingTable.checkTableBeenLoad();
    statistics.shouldBe(visible);
    return this;
  }
}
