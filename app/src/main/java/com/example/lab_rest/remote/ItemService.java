package com.example.lab_rest.remote;

import com.example.lab_rest.model.RecyclableItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ItemService {

    @GET("item")
    Call<List<RecyclableItem>> getAllItem(@Header("api-key") String api_key);
}
