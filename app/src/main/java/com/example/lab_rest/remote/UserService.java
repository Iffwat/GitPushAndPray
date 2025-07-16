package com.example.lab_rest.remote;

import com.example.lab_rest.model.AdminRequestModel;
import com.example.lab_rest.model.LoginRequest;
import com.example.lab_rest.model.LoginResponse;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestHistoryModel;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.SubmitRequest;
import com.example.lab_rest.model.UpdateRequestModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    // Login user or admin
    @Headers("Content-Type: application/json")
    @POST("login.php")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // You can add more endpoints here later:
    // e.g., register, submitRequest, viewMyRequests, updateRequest, etc.
    @Headers("Content-Type: application/json")
    @POST("submit-request")
    Call<Map<String, String>> submitRequest(@Body SubmitRequest request);

    @Headers("Content-Type: application/json")
    @POST("submit-request")
    Call<Void> submitRequest(@Body RequestModel request);
    @DELETE("request/{id}")
    Call<Void> cancelRequest(@Path("id") int requestId);


    @GET("get-items")
    Call<List<RecyclableItem>> getRecyclableItems();

    @GET("my-requests/{userId}")
    Call<List<RequestHistoryModel>> getMyRequests(@Path("userId") int userId);

    @Headers("Content-Type: application/json")
    @POST("update-request")
    Call<Map<String, String>> updateRequest(@Body UpdateRequestModel model);

    @GET("all-requests")
    Call<List<AdminRequestModel>> getAllRequests();

    @POST("recyclable-items")
    Call<Void> addRecyclableItem(@Body RecyclableItem item);

    @PUT("recyclable-items/{id}")
    Call<Void> updateRecyclableItem(@Path("id") int id, @Body RecyclableItem item);

    @DELETE("recyclable-items/{id}")
    Call<Void> deleteRecyclableItem(@Path("id") int id);







}
