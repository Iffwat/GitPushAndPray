package com.example.lab_rest.remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestHistoryModel;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.User;

import java.util.List;

public interface UserService {

    @FormUrlEncoded
    @POST("users/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("users/login")
    Call<User> loginEmail(@Field("email") String username, @Field("password") String password);

    @GET("recyclable_items")
    Call<List<RecyclableItem>> getRecyclableItems();

    @POST("submit_request")
    Call<Void> submitRequest(@Body RequestModel request);

    @GET("user_requests/{user_id}")
    Call<List<RequestHistoryModel>> getUserRequests(@Path("user_id") int userId);

    @DELETE("cancel_request/{request_id}")
    Call<Void> cancelRequest(@Path("request_id") int requestId);


}
