package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
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
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

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
          description = "Обучение Niffler 2.0",
          amount = 89000.00,
          currency = CurrencyValues.RUB
      )
  )
  @ScreenShotTest(value = "img/expected-stat-edit.png", rewriteExpected = true)
  void checkStatComponentAfterEditingTest(UserDataJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getSpendingTable()
        .editSpending(user.testData().spendings().getFirst().description())
        .setAmount(82500.99).save();

    new MainPage()
        .getStatComponent()
        .checkStatisticImage(expected)
        .checkStatisticBubblesContains("Обучение 82500.99 ₽");
  }

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990.19,
          currency = CurrencyValues.RUB
      )
  )
  @ScreenShotTest(value = "img/expected-stat.png", rewriteExpected = true)
  void checkStatComponentTest(UserDataJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getStatComponent()
        .checkStatisticImage(expected)
        .checkStatisticBubblesContains("Обучение 79990.19 ₽");
  }

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Niffler 2.0",
          amount = 89000.00,
          currency = CurrencyValues.RUB
      )
  )
  @ScreenShotTest(value = "img/expected-stat-delete.png", rewriteExpected = true)
  void checkStatComponentAfterDeletingSpendTest(UserDataJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getSpendingTable()
        .deleteSpending(user.testData().spendings().getFirst().description());

    new MainPage()
        .getStatComponent()
        .checkStatisticImage(expected);
  }

  @User(
      categories = {
          @Category(name = "Поездки"),
          @Category(name = "Ремонт", archived = true),
          @Category(name = "Страховка", archived = true)
      },
      spendings = {
          @Spending(
              category = "Поездки",
              description = "В Москву",
              amount = 9500,
              currency = CurrencyValues.RUB
          ),
          @Spending(
              category = "Ремонт",
              description = "Цемент",
              amount = 100,
              currency = CurrencyValues.RUB
          ),
          @Spending(
              category = "Страховка",
              description = "ОСАГО",
              amount = 3000,
              currency = CurrencyValues.RUB
          )
      }
  )
  @ScreenShotTest(value = "img/expected-stat-archived.png", rewriteExpected = true)
  void checkStatComponentWithArchiveCategoryTest(UserDataJson user, BufferedImage expected) throws IOException {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(user.username(), user.testData().password())
        .getStatComponent()
        .checkStatisticBubblesContains("Поездки 9500 ₽", "Archived 3100 ₽")
        .checkStatisticImage(expected)
        .checkBubbles(Color.yellow, Color.green);
  }
}
