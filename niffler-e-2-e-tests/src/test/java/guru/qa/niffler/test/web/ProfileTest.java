package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User(
      categories = @Category(
          archived = true
      )
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(UserDataJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goProfilePage()
        .checkThatPageLoaded()
        .switchArchivedCategories()
        .checkCategoryExist(user.testData().categories().getFirst().name());
  }

  @User(
      categories = @Category(
          archived = false
      )
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(UserDataJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goProfilePage()
        .checkThatPageLoaded()
        .checkCategoryExist(user.testData().categories().getFirst().name());
  }

  @User
  @Test
  void profileNameMayBeUpdated(UserDataJson user) {
    String newName = RandomDataUtils.randomName();

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .checkThatPageLoaded()
        .getHeader()
        .goProfilePage()
        .checkThatPageLoaded()
        .setName(newName)
        .checkAlert("Profile successfully updated");

    Selenide.refresh();
    new ProfilePage().checkName(newName);
  }

  @User
  @ScreenShotTest(value = "img/expected-avatar.png", rewriteExpected = true)
  void checkProfileImageTest(UserDataJson user, BufferedImage expectedAvatar) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader().goProfilePage()
        .uploadAvatar("img/dino.png")
        .checkAvatar(expectedAvatar);
  }
}
