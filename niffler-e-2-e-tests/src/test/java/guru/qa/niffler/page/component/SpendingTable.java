package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent {

  private final ElementsCollection tableRows = self.$$("tbody tr");
  private final SelenideElement deleteBtn = self.$("#delete");
  private final SelenideElement dialogWindow = $("div[role='dialog']");
  private final SelenideElement periodMenuBtn = self.$("#period");
  private final ElementsCollection periodMenuItems = $("ul[role='listbox']").$$("li");
  private final SearchField spendingSearch = new SearchField();

  public SpendingTable() {
    super($("#spendings"));
  }

  public enum DataFilterValues {
    ALL,
    MONTH,
    WEEK,
    TODAY
  }

  @Step("Select '{period}' spending period")
  @Nonnull
  public SpendingTable selectPeriod(DataFilterValues period) {
    periodMenuBtn.click();
    periodMenuItems.find(attribute("data-value", period.toString())).click();
    return this;
  }

  @Step("Open edit spending form for spending with description '{spendingDescription}'")
  @Nonnull
  public EditSpendingPage editSpending(String description) {
    searchSpendingByDescription(description);
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Delete spending with description '{description}'")
  @Nonnull
  public SpendingTable deleteSpending(String description) {
    searchSpendingByDescription(description);
    tableRows.find(text(description))
        .$$("td")
        .get(0)
        .click();
    deleteBtn.click();
    dialogWindow.$(byText("Delete")).click();
    return this;
  }

  @Step("Search spending with description '{description}'")
  @Nonnull
  public SpendingTable searchSpendingByDescription(String description) {
    spendingSearch.search(description);
    return this;
  }

  @Step("Check that table contains '{expectedSpend}'")
  @Nonnull
  public SpendingTable checkTableContains(String expectedSpend) {
    searchSpendingByDescription(expectedSpend);
    tableRows.find(text(expectedSpend)).shouldBe(visible);
    return this;
  }

  @Step("Check spending table size")
  public SpendingTable checkTableSize(int expectedSize) {
    tableRows.shouldHave(size(expectedSize));
    return this;
  }

  @Step("Check that spending table been load")
  @Nonnull
  public SpendingTable checkTableBeenLoad() {
    self.shouldBe(visible);
    return this;
  }
}
