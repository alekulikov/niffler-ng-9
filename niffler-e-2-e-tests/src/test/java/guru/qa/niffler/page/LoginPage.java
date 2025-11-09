package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

  private final SelenideElement usernameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement submitButton;
  private final SelenideElement registerLink;

  public LoginPage(SelenideDriver driver) {
    this.usernameInput = driver.$("input[name='username']");
    this.passwordInput = driver.$("input[name='password']");
    this.submitButton = driver.$("button[type='submit']");
    this.registerLink = driver.$(By.linkText("Create new account"));
    WebDriverRunner.setWebDriver(driver.getWebDriver());
  }

  public LoginPage() {
    this.usernameInput = $("input[name='username']");
    this.passwordInput = $("input[name='password']");
    this.submitButton = $("button[type='submit']");
    this.registerLink = $(By.linkText("Create new account"));
  }

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
