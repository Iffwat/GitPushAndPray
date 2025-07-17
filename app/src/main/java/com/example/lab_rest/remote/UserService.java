package com.example.lab_rest.remote;

import com.example.lab_rest.model.AdminRequestModel;
import com.example.lab_rest.model.LoginRequest;
import com.example.lab_rest.model.LoginResponse;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestHistoryModel;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.SubmitRequest;
import com.example.lab_rest.model.UpdateRequestModel;
import com.example.lab_rest.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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


    // User Requests
    @GET("user/requests/{userId}")
    Call<List<RequestHistoryModel>> getUserRequests(@Path("userId") int userId);

    @DELETE("requests/{id}")
    Call<Void> cancelRequest(@Path("id") int requestId);

    @Headers("Content-Type: application/json")
    @POST("requests")
    Call<Void> submitRequest(@Body RequestModel request);

    // Admin Requests
    @GET("requests")
    Call<List<AdminRequestModel>> getAllRequests(@Header("api-key") String api_key);

    @Headers("Content-Type: application/json")
    @POST("requests")
    Call<Map<String, String>> updateRequest(@Body UpdateRequestModel model);

    // Recyclable Item Management (CRUD)
    @GET("recyclable_items")
    Call<List<RecyclableItem>> getRecyclableItems(@Header("api-key") String api_key);

    @POST("recyclable_items")
    Call<Void> addRecyclableItem(@Body RecyclableItem item);

    @PUT("recyclable_items/{id}")
    Call<Void> updateRecyclableItem(@Path("id") int id, @Body RecyclableItem item);

    @DELETE("recyclable_items/{id}")
    Call<Void> deleteRecyclableItem(@Path("id") int id);
}

