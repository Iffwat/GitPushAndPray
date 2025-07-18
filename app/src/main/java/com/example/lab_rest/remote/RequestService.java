package com.example.lab_rest.remote;

import com.example.lab_rest.model.RequestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestService {

    @GET("requests")
    Call<List<RequestModel>> getUserRequests(@Header("api-key") String api_key);

    @DELETE("requests/{id}")
    Call<Void> cancelRequest(@Header("api-key") String api_key, @Path("id") int requestId);

    @FormUrlEncoded
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
    Call<List<RequestModel>> getAllRequests(@Header("api-key") String api_key);

    @GET("request/{id}")
    Call<RequestModel> getRequest(@Header("api-key") String api_key, @Path("id") int id);

    @FormUrlEncoded
    @POST("requests/{id}")
    Call<RequestModel> updateRequest(@Header("api-key") String api_key, @Path("id") int id,
                                     @Field("user_id") int user_id,
                                     @Field("item_id") int item_id,
                                     @Field("address") String address,
                                     @Field("status") String status,
                                     @Field("weight") double weight,
                                     @Field("total_price") double total_price,
                                     @Field("notes") String notes);
}
