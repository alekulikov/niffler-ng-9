package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "duck",
      categories = @Category(
          archived = true
      )
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson... categories) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .getHeader()
        .goProfilePage()
        .checkThatPageLoaded()
        .switchArchivedCategories()
        .checkCategoryExist(categories[0].name());
  }

  @User(
      username = "duck",
      categories = @Category(
          archived = false
      )
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson... categories) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .getHeader()
        .goProfilePage()
        .checkThatPageLoaded()
        .checkCategoryExist(categories[0].name());
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
}
