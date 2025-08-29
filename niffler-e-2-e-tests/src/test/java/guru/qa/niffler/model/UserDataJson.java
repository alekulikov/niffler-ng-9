package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.UserdataUserEntity;

import java.util.UUID;

public record UserDataJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("fullname")
    String fullname,
    @JsonProperty("photo")
    byte[] photo,
    @JsonProperty("photoSmall")
    byte[] photoSmall,
    @JsonProperty("friendshipStatus")
    FriendshipStatus friendshipStatus,
    @JsonIgnore
    TestData testData) {

  public static UserDataJson fromEntity(UserdataUserEntity entity, FriendshipStatus friendshipStatus) {
    return new UserDataJson(
        entity.getId(),
        entity.getUsername(),
        entity.getCurrency(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getFullname(),
        entity.getPhoto(),
        entity.getPhotoSmall(),
        friendshipStatus,
        new TestData(null)
    );
  }

  public UserDataJson withTestData(TestData testData) {
    return new UserDataJson(
        id,
        username,
        currency,
        firstname,
        surname,
        fullname,
        photo,
        photoSmall,
        friendshipStatus,
        testData
    );
  }

  public UserDataJson withPassword(String password) {
    return new UserDataJson(
        id,
        username,
        currency,
        firstname,
        surname,
        fullname,
        photo,
        photoSmall,
        friendshipStatus,
        new TestData(
            password,
            testData.friends(),
            testData.incomeInvitations(),
            testData.outcomeInvitations(),
            testData.categories(),
            testData.spendings()
        )
    );
  }
}
