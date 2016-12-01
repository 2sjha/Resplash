package io.github.b_lam.resplash.data.api;


import io.github.b_lam.resplash.data.data.Me;
import io.github.b_lam.resplash.data.data.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * User api.
 * */

public interface UserApi {

    @GET("users/{username}")
    Call<User> getUserProfile(@Path("username") String username,
                              @Query("w") int w,
                              @Query("h") int h);

    @GET("me")
    Call<Me> getMeProfile();

    @PUT("me")
    Call<Me> updateMeProfile(@Query("username") String username,
                             @Query("first_name") String first_name,
                             @Query("last_name") String last_name,
                             @Query("email") String email,
                             @Query("url") String url,
                             @Query("location") String location,
                             @Query("bio") String bio);
}
