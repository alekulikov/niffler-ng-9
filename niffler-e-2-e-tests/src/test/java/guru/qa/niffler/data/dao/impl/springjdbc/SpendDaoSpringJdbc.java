package guru.qa.niffler.data.dao.impl.springjdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendDaoSpringJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  @Nonnull
  public SpendEntity create(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          """
              INSERT INTO spend (username, spend_date, currency, amount, description, category_id)
              VALUES ( ?, ?, ?, ?, ?, ?)
              """,
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, spend.getUsername());
      ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());
      return ps;
    }, kh);

    spend.setId((UUID) kh.getKeys().get("id"));
    return spend;
  }

  @Override
  @Nonnull
  public Optional<SpendEntity> findSpendById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(
          "SELECT * FROM spend WHERE id = ?",
          SpendEntityRowMapper.instance,
          id)
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  @Nonnull
  public List<SpendEntity> findAllByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.query(
        "SELECT * FROM spend WHERE username = ?",
        SpendEntityRowMapper.instance, username
    );
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    jdbcTemplate.update("DELETE FROM spend WHERE id = ?", spend.getId());
  }

  @Override
  @Nonnull
  public List<SpendEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.query(
        "SELECT * FROM spend",
        SpendEntityRowMapper.instance
    );
  }

  @Override
  @Nonnull
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(
          "SELECT * FROM spend WHERE username = ? AND description = ?",
          SpendEntityRowMapper.instance,
          username, description)
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  @Nonnull
  public SpendEntity update(SpendEntity spend) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    jdbcTemplate.update(
        """
            UPDATE spend SET username = ?,
                             spend_date = ?,
                             currency = ?,
                             amount = ?,
                             description = ?,
                             category_id = ?
            WHERE id = ?
            """, spend.getUsername(), spend.getSpendDate(), spend.getCurrency().name(),
        spend.getAmount(), spend.getDescription(), spend.getCategory().getId(), spend.getId()
    );
    return spend;
  }
}
