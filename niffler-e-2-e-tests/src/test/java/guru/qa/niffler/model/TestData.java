package guru.qa.niffler.model;

import java.util.ArrayList;
import java.util.List;

public record TestData(
    String password,
    List<UserDataJson> friends,
    List<UserDataJson> incomeInvitations,
    List<UserDataJson> outcomeInvitations,
    List<CategoryJson> categories,
    List<SpendJson> spendings
) {

  public TestData(String password) {
    this(password,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>()
    );
  }
}
