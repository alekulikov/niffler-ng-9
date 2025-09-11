package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement passwordConfirmInput = $("input[name='passwordSubmit']");
  private final SelenideElement submitBtn = $("button[type='submit']");
  private final SelenideElement message = $("p.form__paragraph_success");
  private final SelenideElement errorMessage = $("span.form__error");

  @Step("Register user '{username}':'{password}'")
  @Nonnull
  public RegisterPage doRegister(String username, String password, String passwordConfirm) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    passwordConfirmInput.setValue(passwordConfirm);
    submitBtn.click();
    return this;
  }

  @Step("Check that message have text '{message}'")
  @Nonnull
  public RegisterPage checkMessageText(String message) {
    this.message.shouldHave(text(message));
    return this;
  }

  @Step("Check that error have text '{message}'")
  @Nonnull
  public RegisterPage checkErrorMessageText(String message) {
    this.errorMessage.shouldHave(text(message));
    return this;
  }
}
