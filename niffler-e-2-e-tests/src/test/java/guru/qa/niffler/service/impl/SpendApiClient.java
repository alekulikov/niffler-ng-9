package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super(CFG.spendUrl());
    spendApi = create(SpendApi.class);
  }

  @Override
  @Nonnull
  @Step("Create spend using REST API")
  public SpendJson createSpend(SpendJson spend) {
    Response<SpendJson> response = execute(
        spendApi.addSpend(spend)
    );
    return Objects.requireNonNull(response.body());
  }

  @Override
  @Nonnull
  @Step("Create category using REST API")
  public CategoryJson createCategory(CategoryJson category) {
    Response<CategoryJson> response = execute(
        spendApi.addCategory(category)
    );
    return Objects.requireNonNull(response.body());
  }

  @Override
  @Nonnull
  @Step("Update category using REST API")
  public CategoryJson updateCategory(CategoryJson category) {
    Response<CategoryJson> response = execute(
        spendApi.updateCategory(category)
    );
    return Objects.requireNonNull(response.body());
  }
}
