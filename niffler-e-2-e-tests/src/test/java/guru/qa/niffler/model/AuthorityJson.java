package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthorityJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("authority")
    Authority authority,
    @JsonProperty("user")
    UserAuthJson user
) {

  @Nonnull
  public static AuthorityJson fromEntity(AuthorityEntity entity) {
    return new AuthorityJson(
        entity.getId(),
        entity.getAuthority(),
        UserAuthJson.fromEntity(entity.getUser())
    );
  }
}