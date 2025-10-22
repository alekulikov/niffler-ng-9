package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement statistics = $("#stat");
  private final SelenideElement statisticDiagram = $("canvas[role='img']");
  private final SelenideElement statisticLegend = $("#legend-container");
  @Getter
  private final Header header = new Header();
  @Getter
  private final SpendingTable spendingTable = new SpendingTable();

  @Step("Check that main page been load")
  @Nonnull
  @Override
  public MainPage checkThatPageLoaded() {
    spendingTable.checkTableBeenLoad();
    statistics.shouldBe(visible);
    return this;
  }

  @Step("Check statistic diagram")
  @Nonnull
  public MainPage checkStatisticDiagram(BufferedImage expected) throws IOException {
    Selenide.sleep(3000);
    BufferedImage actual = ImageIO.read(Objects.requireNonNull(statisticDiagram.screenshot()));
    assertFalse(new ScreenDiffResult(
        actual,
        expected
    ));
    return this;
  }

  @Step("Check statistic legend")
  @Nonnull
  public MainPage checkStatisticLegend(List<String> spends) {
    for (String description : spends) {
      statisticLegend.shouldHave(text(description));
    }
    return this;
  }
}
