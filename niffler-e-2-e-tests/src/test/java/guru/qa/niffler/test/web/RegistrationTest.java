package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
class RegistrationTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void shouldRegisterNewUser() {
    String username = randomUsername();
    String password = "12345";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .goRegisterPage()
        .doRegister(username, password, password)
        .checkMessageText("Congratulations! You've registered!");
  }

  @User
  @Test
  void shouldNotRegisterUserWithExistingUsername(UserDataJson user) {
    String username = user.username();
    String password = user.testData().password();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .goRegisterPage()
        .doRegister(username, password, password)
        .checkErrorMessageText(String.format("Username `%s` already exists", username));
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    String username = randomUsername();
    String password = "12345";
    String confirmPassword = "0123456";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .goRegisterPage()
        .checkThatPageLoaded()
        .doRegister(username, password, confirmPassword)
        .checkErrorMessageText("Passwords should be equal");
  }
}
