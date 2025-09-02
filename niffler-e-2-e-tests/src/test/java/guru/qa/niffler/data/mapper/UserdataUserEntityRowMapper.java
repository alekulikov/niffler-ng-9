package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserEntityRowMapper implements RowMapper<UserdataUserEntity> {

  public static final UserdataUserEntityRowMapper instance = new UserdataUserEntityRowMapper();

  private UserdataUserEntityRowMapper() {
  }

  @Override
  @Nonnull
  public UserdataUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserdataUserEntity result = new UserdataUserEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    result.setFirstname(rs.getString("firstname"));
    result.setSurname(rs.getString("surname"));
    result.setFullname(rs.getString("full_name"));
    result.setPhoto(rs.getBytes("photo"));
    result.setPhotoSmall(rs.getBytes("photo_small"));
    return result;
  }
}
