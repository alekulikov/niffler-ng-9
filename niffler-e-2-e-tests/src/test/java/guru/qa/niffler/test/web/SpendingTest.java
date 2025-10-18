package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Niffler 2.0",
          amount = 89000.00,
          currency = CurrencyValues.RUB
      )
  )
  @Test
  void spendingDescriptionMayBeUpdatedByTableAction(UserDataJson user) {
    final String newDescription = ":)";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getSpendingTable()
        .editSpending(user.testData().spendings().getFirst().description())
        .setSpendingDescription(newDescription)
        .save()
        .getSpendingTable()
        .checkTableContains(newDescription);
  }

  @User(
      categories = @Category(
          name = "New Category"
      )
  )
  @Test
  void newSpendingMayBeAdded(UserDataJson user) {
    SpendJson newSpending = new SpendJson(
        null,
        new Date(),
        user.testData().categories().getFirst(),
        CurrencyValues.RUB,
        100.0,
        RandomDataUtils.randomSentence(2),
        user.username()
    );

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getHeader()
        .goAddSpendingPage()
        .addNewSpending(newSpending)
        .checkAlert("New spending is successfully created")
        .getSpendingTable()
        .checkTableContains(newSpending.description());
  }

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990,
          currency = CurrencyValues.RUB
      )
  )
  @ScreenShotTest("img/expected-stat.png")
  void checkStatComponentTest(UserDataJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password());

    BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());
    assertFalse(new ScreenDiffResult(
        expected,
        actual
    ));
  }
}
