package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

  SpendEntity create(SpendEntity spend);

  CategoryEntity createCategory(CategoryEntity category);

  SpendEntity update(SpendEntity spend);

  CategoryEntity updateCategory(CategoryEntity category);

  Optional<SpendEntity> findById(UUID id);

  Optional<CategoryEntity> findCategoryById(UUID id);

  Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

  Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name);

  void remove(SpendEntity spend);

  void removeCategory(CategoryEntity category);
}
