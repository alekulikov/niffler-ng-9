package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendRepositoryJdbc implements SpendRepository {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  @Override
  @Nonnull
  public SpendEntity create(SpendEntity spend) {
    if (spend.getCategory().getId() == null) {
      categoryDao.findCategoryByUsernameAndCategoryName(
              spend.getCategory().getUsername(), spend.getCategory().getName())
          .ifPresentOrElse(
              ce -> spend.setCategory(ce),
              () -> spend.setCategory(categoryDao.create(spend.getCategory()))
          );
    }
    return spendDao.create(spend);
  }

  @Override
  @Nonnull
  public SpendEntity update(SpendEntity spend) {
    return spendDao.update(spend);
  }

  @Override
  @Nonnull
  public CategoryEntity createCategory(CategoryEntity category) {
    return categoryDao.findCategoryByUsernameAndCategoryName(
        category.getUsername(), category.getName()
    ).orElse(categoryDao.create(category));
  }

  @Override
  @Nonnull
  public Optional<SpendEntity> findById(UUID id) {
    return spendDao.findSpendById(id);
  }

  @Override
  @Nonnull
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
    return spendDao.findByUsernameAndSpendDescription(username, description);
  }

  @Override
  @Nonnull
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return categoryDao.findCategoryById(id);
  }

  @Override
  @Nonnull
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
    return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
  }

  @Override
  public void remove(SpendEntity spend) {
    spendDao.deleteSpend(spend);
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    categoryDao.deleteCategory(category);
  }

  @Override
  @Nonnull
  public CategoryEntity updateCategory(CategoryEntity category) {
    return categoryDao.updateCategory(category);
  }
}
