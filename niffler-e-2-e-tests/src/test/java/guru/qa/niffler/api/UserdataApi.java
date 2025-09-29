package guru.qa.niffler.api;

import guru.qa.niffler.model.UserDataJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserdataApi {

  @GET("internal/users/current")
  Call<UserDataJson> currentUser(@Query("username") String username);

  @POST("internal/users/update")
  Call<UserDataJson> updateUserInfo(@Body UserDataJson user);

  @GET("internal/users/all")
  Call<List<UserDataJson>> allUsers(@Query("username") String username,
                                    @Query("searchQuery") String searchQuery);

  @GET("internal/friends/all")
  Call<List<UserDataJson>> friends(@Query("username") String username,
                                   @Query("searchQuery") String searchQuery);

  @DELETE("internal/friends/remove")
  Call<Void> removeFriend(@Query("username") String username,
                          @Query("targetUsername") String targetUsername);

  @POST("internal/invitations/accept")
  Call<UserDataJson> acceptInvitation(@Query("username") String username,
                                      @Query("targetUsername") String targetUsername);

  @POST("internal/invitations/decline")
  Call<UserDataJson> declineInvitation(@Query("username") String username,
                                       @Query("targetUsername") String targetUsername);

  @POST("internal/invitations/send")
  Call<UserDataJson> sendInvitation(@Query("username") String username,
                                    @Query("targetUsername") String targetUsername);
}
