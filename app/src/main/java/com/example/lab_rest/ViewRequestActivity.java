package com.example.lab_rest;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.RequestAdapter;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.ItemService;
import com.example.lab_rest.remote.RequestService;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerRequests;
    private RequestAdapter requestAdapter;
    private List<RequestModel> requestList;
    private UserService userService;
    private ItemService itemService;
    private RequestService requestService;
    private Map<Integer, String> itemNameMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        recyclerRequests = findViewById(R.id.recyclerRequests);
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        userService = ApiUtils.getUserService();
        itemService = ApiUtils.getItemService();
        requestService = ApiUtils.getRequestService();
        String token = new SharedPrefManager(this).getUser().getToken();

        // Load item names first, then load requests
        loadItemNamesAndRequests(token);
    }

    private void loadItemNamesAndRequests(String token) {
        itemService.getRecyclableItems(token).enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (RecyclableItem item : response.body()) {
                        itemNameMap.put(item.getItemId(), item.getItemName());
                    }
                    setupAdapter(); // Now safe to setup adapter
                    loadMyRequests(token);
                } else {
                    Toast.makeText(ViewRequestActivity.this, "Failed to load item names", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecyclableItem>> call, Throwable t) {
                Toast.makeText(ViewRequestActivity.this, "Error loading items: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter() {
        requestAdapter = new RequestAdapter(requestList, itemNameMap);
        recyclerRequests.setAdapter(requestAdapter);

        requestAdapter.setCancelClickListener((requestId, position) -> {
            String token = new SharedPrefManager(this).getUser().getToken();
            requestService.cancelRequest(token, requestId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        requestList.remove(position);
                        requestAdapter.notifyItemRemoved(position);
                        Toast.makeText(ViewRequestActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewRequestActivity.this, "Failed to cancel request", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ViewRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadMyRequests(String token) {
        requestService.getUserRequests(token).enqueue(new Callback<List<RequestModel>>() {
            @Override
            public void onResponse(Call<List<RequestModel>> call, Response<List<RequestModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requestList.clear();
                    requestList.addAll(response.body());
                    requestAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewRequestActivity.this, "No requests found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RequestModel>> call, Throwable t) {
                Toast.makeText(ViewRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
