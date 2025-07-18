package com.example.lab_rest.remote;


import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.RecyclableItem;

import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    // Login
    @FormUrlEncoded
    @POST("users/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("users/login")
    Call<User> loginEmail(@Field("email") String email, @Field("password") String password);



    // ✅ NEW METHOD TO GET ALL USERS FOR ADMIN VIEW
    @GET("users")
    Call<List<User>> getAllUsers(@Header("api-key") String api_key);
}
