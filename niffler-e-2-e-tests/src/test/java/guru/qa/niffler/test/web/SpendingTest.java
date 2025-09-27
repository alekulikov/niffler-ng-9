package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

@WebTest
class SpendingTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "duck",
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Niffler 2.0",
          amount = 89000.00,
          currency = CurrencyValues.RUB
      )
  )
  @Test
  void spendingDescriptionMayBeUpdatedByTableAction(SpendJson... spendings) {
    final String newDescription = ":)";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("duck", "12345")
        .getSpendingTable()
        .editSpending(spendings[0].description())
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
}
