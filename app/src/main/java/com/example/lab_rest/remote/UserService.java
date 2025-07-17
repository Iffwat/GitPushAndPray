package com.example.lab_rest.remote;

import com.example.lab_rest.model.AdminRequestModel;
import com.example.lab_rest.model.DeleteResponse;
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
    @GET("requests")
    Call<List<RequestHistoryModel>> getUserRequests(@Header("api-key") String api_key);

    @DELETE("requests/{id}")
    Call<Void> cancelRequest(@Header("api-key") String api_key, @Path("id") int requestId);


    @POST("requests")
    Call<Void> submitRequest(@Header("api-key") String api_key,
                             @Field("user_id") int user_id,
                             @Field("item_id") int item_id,
                             @Field("address") String address,
                             @Field("request_date") String request_date,
                             @Field("status") String status,
                             @Field("weight") double weight,
                             @Field("total_price") double total_price,
                             @Field("notes") String notes);

    // Admin Requests
    @GET("requests")
    Call<List<AdminRequestModel>> getAllRequests(@Header("api-key") String api_key);

    @GET("request/{id}")
    Call<RequestModel> getRequest(@Header("api-key") String api_key, @Path("id") int id);


    @POST("requests/{id}")
    Call<RequestModel> updateRequest(@Header("api-key") String api_key,@Path("id") int id,
                                      @Field("user_id") int user_id,
                                      @Field("item_id") int item_id,
                                      @Field("address") String address,
                                      @Field("request_date") String request_date,
                                      @Field("status") String status,
                                      @Field("weight") double weight,
                                      @Field("total_price") double total_price,
                                      @Field("notes") String notes);

    // Recyclable Item Management (CRUD)
    @GET("recyclable_items")
    Call<List<RecyclableItem>> getRecyclableItems(@Header("api-key") String api_key);

    @POST("recyclable_items")
    Call<Void> addRecyclableItem(@Header("api-key") String api_key,@Body RecyclableItem item);

    @PUT("recyclable_items/{id}")
    Call<Void> updateRecyclableItem(@Header("api-key") String api_key, @Path("id") int id, @Body RecyclableItem item);

    @DELETE("recyclable_items/{id}")
    Call<DeleteResponse> deleteRecyclableItem(@Header("api-key") String api_key, @Path("id") int id);
}

