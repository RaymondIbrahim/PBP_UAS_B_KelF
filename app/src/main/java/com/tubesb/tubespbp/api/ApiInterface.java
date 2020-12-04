package com.tubesb.tubespbp.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user")
    Call<com.tubesb.tubespbp.api.UserResponse> getAllUser(@Query("data") String data);

    @GET("user/{email}")
    Call<UserResponse> getUserByEmail(@Path("email") String email,
                                   @Query("data") String data);


    @POST("userupdate/{id}")
    @FormUrlEncoded
    Call<UserResponse> updateUser(@Path("id") String id,
                                  @Field("name") String nama,
                                  @Field("alamat") String alamat,
                                  @Field("noTelp") String noTelp);

    @POST("userdelete{id}")
    Call<com.tubesb.tubespbp.api.UserResponse> deleteUser(@Path("id") String id);

    @POST("register")
    @FormUrlEncoded
    Call<UserResponse> createUser(@Field("email") String email,
                                  @Field("password") String password,
                                  @Field("name") String nama,
                                  @Field("alamat") String alamat,
                                  @Field("noTelp") String noTelp);

    @POST("login")
    @FormUrlEncoded
    Call<UserResponse> loginUser(@Field("email") String email,
                                 @Field("password") String password);
}
