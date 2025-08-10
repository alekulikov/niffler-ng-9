package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  private final CategoryDao categoryDaoJdbc = new CategoryDaoJdbc();
  private final SpendDao spendDaoJdbc = new SpendDaoJdbc();
  private final CategoryDao categoryDaoSpringJdbc = new CategoryDaoSpringJdbc(DataSources.dataSource(CFG.spendJdbcUrl()));
  private final SpendDao spendDaoSpringJdbc = new SpendDaoSpringJdbc(DataSources.dataSource(CFG.spendJdbcUrl()));

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.spendJdbcUrl()
  );
  private final TransactionTemplate springTxTemplate = new TransactionTemplate(
      new JdbcTransactionManager(DataSources.dataSource(CFG.spendJdbcUrl()))
  );

  public SpendJson createSpendSpringJdbc(SpendJson spend) {
    return springTxTemplate.execute(status -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          CategoryEntity categoryEntity = spendEntity.getCategory();
          if (categoryEntity.getId() == null) {
            categoryDaoSpringJdbc.findCategoryByUsernameAndCategoryName(
                    categoryEntity.getUsername(), categoryEntity.getName())
                .ifPresentOrElse(
                    ce -> spendEntity.setCategory(ce),
                    () -> spendEntity.setCategory(categoryDaoSpringJdbc.create(categoryEntity))
                );
          }
          return SpendJson.fromEntity(spendDaoSpringJdbc.create(spendEntity));
        }
    );
  }

  public CategoryJson createCategorySpringJdbc(CategoryJson category) {
    return springTxTemplate.execute(status -> {
          CategoryEntity categoryEntity = categoryDaoSpringJdbc.create(CategoryEntity.fromJson(category));
          return CategoryJson.fromEntity(categoryEntity);
        }
    );
  }

  public SpendJson createSpend(SpendJson spend) {
    return jdbcTxTemplate.execute(() -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          CategoryEntity categoryEntity = spendEntity.getCategory();
          if (categoryEntity.getId() == null) {
            categoryDaoJdbc.findCategoryByUsernameAndCategoryName(
                    categoryEntity.getUsername(), categoryEntity.getName())
                .ifPresentOrElse(
                    ce -> spendEntity.setCategory(ce),
                    () -> spendEntity.setCategory(categoryDaoJdbc.create(categoryEntity))
                );
          }
          return SpendJson.fromEntity(spendDaoJdbc.create(spendEntity)
          );
        }
    );
  }

  public CategoryJson createCategory(CategoryJson category) {
    return jdbcTxTemplate.execute(() -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      return CategoryJson.fromEntity(categoryDaoJdbc.create(categoryEntity));
    });
  }

  public CategoryJson updateCategory(CategoryJson category) {
    return jdbcTxTemplate.execute(() -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      return CategoryJson.fromEntity(categoryDaoJdbc.updateCategory(categoryEntity));
    });
  }
}