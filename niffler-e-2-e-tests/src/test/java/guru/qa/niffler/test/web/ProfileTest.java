package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @Category(
      username = "duck",
      archived = true
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .goProfilePage()
        .switchArchivedCategories()
        .checkCategoryExist(category.name());
  }

  @Category(
      username = "duck",
      archived = false
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .goProfilePage()
        .checkCategoryExist(category.name());
  }
}
