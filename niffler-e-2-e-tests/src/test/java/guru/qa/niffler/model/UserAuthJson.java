package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserAuthJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("password")
    String password,
    @JsonProperty("enabled")
    Boolean enabled,
    @JsonProperty("accountNonExpired")
    Boolean accountNonExpired,
    @JsonProperty("accountNonLocked")
    Boolean accountNonLocked,
    @JsonProperty("credentialsNonExpired")
    Boolean credentialsNonExpired) {

  @Nonnull
  public static UserAuthJson fromEntity(AuthUserEntity entity) {
    return new UserAuthJson(
        entity.getId(),
        entity.getUsername(),
        entity.getPassword(),
        entity.getEnabled(),
        entity.getAccountNonExpired(),
        entity.getAccountNonLocked(),
        entity.getCredentialsNonExpired()
    );
  }
}
