package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement submitButton = $("#save");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");
  private final Calendar calendar = new Calendar();

  @Step("Add new spending")
  @Nonnull
  public MainPage addNewSpending(SpendJson spend) {
    setSpendingCategory(spend.category().name())
        .setSpendingDescription(spend.description())
        .setAmount(spend.amount())
        .calendar.selectDateInCalendar(spend.spendDate());
    return save();
  }

  @Step("Set description '{description}'")
  @Nonnull
  public EditSpendingPage setSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Set category '{category}'")
  @Nonnull
  public EditSpendingPage setSpendingCategory(String category) {
    categoryInput.clear();
    categoryInput.setValue(category);
    return this;
  }

  @Step("Set amount '{amount}'")
  @Nonnull
  public EditSpendingPage setAmount(Double amount) {
    amountInput.clear();
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Save spending")
  @Nonnull
  public MainPage save() {
    submitButton.click();
    return new MainPage();
  }

  @Step("Check that edit spending page been load")
  @Nonnull
  @Override
  public EditSpendingPage checkThatPageLoaded() {
    submitButton.shouldBe(visible);
    return this;
  }
}
