package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private final SpendRepository spendRepository = new SpendRepositoryHibernate();
  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(CFG.spendJdbcUrl());

  @Override
  @Nonnull
  @Step("Create spend using SQL")
  public SpendJson createSpend(SpendJson spend) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
        SpendJson.fromEntity(spendRepository.create(SpendEntity.fromJson(spend)))));
  }

  @Override
  @Nonnull
  @Step("Create category using SQL")
  public CategoryJson createCategory(CategoryJson category) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
        CategoryJson.fromEntity(spendRepository.createCategory(CategoryEntity.fromJson(category)))
    ));
  }

  @Override
  @Nonnull
  @Step("Update category using SQL")
  public CategoryJson updateCategory(CategoryJson category) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
        CategoryJson.fromEntity(spendRepository.updateCategory(CategoryEntity.fromJson(category)))
    ));
  }
}
