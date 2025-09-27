package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {

  private final SelenideElement alert = $(".MuiSnackbar-root");

  public abstract T checkThatPageLoaded();

  @Step("Check that alert message have text: '{expectedText}'")
  @SuppressWarnings("unchecked")
  @Nonnull
  public T checkAlert(String text) {
    alert.shouldBe(visible).shouldHave(text(text));
    return (T) this;
  }
}
