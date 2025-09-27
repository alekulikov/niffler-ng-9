package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerLink = $(By.linkText("Create new account"));

  @Step("Login with '{username}':'{password}'")
  @Nonnull
  public MainPage doLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  @Step("Go to register page")
  @Nonnull
  public RegisterPage goRegisterPage() {
    registerLink.click();
    return new RegisterPage();
  }

  @Step("Check that login page been load")
  @Nonnull
  @Override
  public LoginPage checkThatPageLoaded() {
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    return this;
  }
}
