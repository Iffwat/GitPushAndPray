package com.example.lab_rest.remote;

import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.RecyclableItem;

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

public interface ItemService {

    @FormUrlEncoded
    @POST("recyclable_items")
    Call<RecyclableItem> addRecyclableItem(@Header("api-key") String api_key,
                                           @Field("item_name") String itemName, @Field("price_per_kg") double pricePerkg);

    // ✅ UPDATE without modifying request_date
    // Recyclable Item Management (CRUD)
    @GET("recyclable_items")
    Call<List<RecyclableItem>> getRecyclableItems(@Header("api-key") String api_key);

    // ✅ NEW: Get a single item by ID (for dynamic price fetching)
    @GET("recyclable_items/{id}")
    Call<RecyclableItem> getItemById(@Header("api-key") String api_key, @Path("id") int id);

    @POST("recyclable_items")
    Call<Void> addRecyclableItem(@Header("api-key") String api_key, @Body RecyclableItem item);

    @PUT("recyclable_items/{id}")
    Call<Void> updateRecyclableItem(@Header("api-key") String api_key, @Path("id") int id, @Body RecyclableItem item);

    @DELETE("recyclable_items/{id}")
    Call<DeleteResponse> deleteRecyclableItem(@Header("api-key") String api_key, @Path("id") int id);

}
