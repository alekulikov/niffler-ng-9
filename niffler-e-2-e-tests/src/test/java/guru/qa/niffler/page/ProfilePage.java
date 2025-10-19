package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProfilePage extends BasePage<ProfilePage> {

  private final SelenideElement archiveCategoriesToggle = $("input.MuiSwitch-input");
  private final ElementsCollection categories = $$(".MuiChip-label");
  private final SelenideElement nameInput = $("input[name='name']");
  private final SelenideElement saveBtn = $("button[type='submit']");
  private final SelenideElement userName = $("#username");
  private final SelenideElement avatar = $(".MuiAvatar-img");
  private final SelenideElement pictureInput = $("input[type='file']");

  @Step("Switch archived categories toggle")
  @Nonnull
  public ProfilePage switchArchivedCategories() {
    archiveCategoriesToggle.click();
    return this;
  }

  @Step("Check that category '{categoryName}' exist")
  @Nonnull
  public ProfilePage checkCategoryExist(String categoryName) {
    categories.find(text(categoryName)).shouldBe(visible);
    return this;
  }

  @Step("Set name: '{name}'")
  @Nonnull
  public ProfilePage setName(String name) {
    nameInput.clear();
    nameInput.setValue(name).pressEnter();
    saveBtn.click();
    return this;
  }

  @Step("Check that name equal '{name}'")
  @Nonnull
  public ProfilePage checkName(String name) {
    nameInput.shouldHave(value(name));
    return this;
  }

  @Step("Check that profile page been load")
  @Nonnull
  @Override
  public ProfilePage checkThatPageLoaded() {
    userName.shouldBe(visible);
    return this;
  }

  @Step("Check profile avatar")
  @Nonnull
  public ProfilePage checkAvatar(BufferedImage expected) throws IOException {
    Selenide.sleep(3000);
    BufferedImage actual = ImageIO.read(avatar.screenshot());
    assertFalse(new ScreenDiffResult(actual, expected));
    return this;
  }

  @Step("Upload profile avatar")
  @Nonnull
  public ProfilePage uploadAvatar(String path) {
    pictureInput.uploadFromClasspath(path);
    return this;
  }
}
