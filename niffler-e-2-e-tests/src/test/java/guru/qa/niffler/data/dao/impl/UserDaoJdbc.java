package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity createUser(UserEntity user) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
         PreparedStatement ps = connection.prepareStatement(
             """
                 INSERT INTO "user" (username, currency, firstname, surname, photo, photo_small, full_name)
                 VALUES (?, ?, ?, ?, ?, ?, ?)
                 """,
             Statement.RETURN_GENERATED_KEYS
         )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          user.setId(rs.getObject("id", UUID.class));
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
         PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity user = new UserEntity();
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setFirstname(rs.getString("firstname"));
          user.setSurname(rs.getString("surname"));
          user.setFullname(rs.getString("full_name"));
          user.setPhoto(rs.getBytes("photo"));
          user.setPhotoSmall(rs.getBytes("photo_small"));
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
         PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"user\" WHERE username = ?")) {
      ps.setString(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity user = new UserEntity();
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setFirstname(rs.getString("firstname"));
          user.setSurname(rs.getString("surname"));
          user.setFullname(rs.getString("full_name"));
          user.setPhoto(rs.getBytes("photo"));
          user.setPhotoSmall(rs.getBytes("photo_small"));
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public void delete(UserEntity user) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
         PreparedStatement ps = connection.prepareStatement("DELETE FROM \"user\" WHERE id = ?")) {
      ps.setObject(1, user.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
