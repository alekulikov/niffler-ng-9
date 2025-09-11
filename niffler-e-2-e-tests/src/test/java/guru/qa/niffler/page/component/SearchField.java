package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField {

  private final SelenideElement searchInputField = $("input[placeholder='Search']");
  private final SelenideElement clearSearchBtn = $("#input-clear");

  @Nonnull
  public SearchField search(String query) {
    clear().searchInputField.setValue(query).pressEnter();
    return this;
  }

  @Nonnull
  public SearchField clear() {
    if (searchInputField.is(not(empty))) {
      clearSearchBtn.click();
      searchInputField.should(empty);
    }
    return this;
  }
}