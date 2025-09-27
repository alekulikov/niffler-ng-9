package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent {

  private final SelenideElement clearSearchBtn = $("#input-clear");

  public SearchField() {
    super($("input[placeholder='Search']"));
  }

  @Nonnull
  public SearchField search(String query) {
    clear().self.setValue(query).pressEnter();
    return this;
  }

  @Nonnull
  public SearchField clear() {
    if (self.is(not(empty))) {
      clearSearchBtn.click();
      self.should(empty);
    }
    return this;
  }
}