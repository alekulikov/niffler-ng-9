package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent {

  private static final DateTimeFormatter HEADER_CALENDAR_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);

  private final SelenideElement calendarButton = $("button[aria-label*='Choose date']");
  private final SelenideElement currentMonthAndYear = self.$(".MuiPickersCalendarHeader-label");
  private final ElementsCollection selectYear = self.$$(".MuiPickersYear-yearButton");
  private final SelenideElement prevMonthButton = self.$("button[title='Previous month']");
  private final SelenideElement nextMonthButton = self.$("button[title='Next month']");
  private final ElementsCollection days = self.$$(".MuiPickersSlideTransition-root button");

  public Calendar() {
    super($(".MuiPickersLayout-root"));
  }

  public void selectDateInCalendar(Date date) {
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    calendarButton.click();
    selectYear(localDate.getYear());
    selectMonth(localDate.getMonthValue());
    selectDay(localDate.getDayOfMonth());
  }

  private void selectYear(int expectedYear) {
    currentMonthAndYear.click();
    selectYear.findBy(text(String.valueOf(expectedYear))).scrollTo().click();
  }

  private void selectMonth(int expectedMonth) {
    int actualMonth = getActualMonth();

    while (actualMonth > expectedMonth) {
      prevMonthButton.click();
      currentMonthAndYear.shouldBe(visible);
      actualMonth = getActualMonth();
    }
    while (actualMonth < expectedMonth) {
      nextMonthButton.click();
      currentMonthAndYear.shouldBe(visible);
      actualMonth = getActualMonth();
    }
  }

  private int getActualMonth() {
    return YearMonth.parse(currentMonthAndYear.getText(), HEADER_CALENDAR_FORMAT).getMonthValue();
  }

  private void selectDay(int expectedDay) {
    days.findBy(text(String.valueOf(expectedDay))).click();
  }
}
