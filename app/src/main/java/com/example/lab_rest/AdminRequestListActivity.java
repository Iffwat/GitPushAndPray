package com.example.lab_rest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.AdminRequestAdapter;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.User;
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

public class AdminRequestListActivity extends AppCompatActivity {

    private RecyclerView recyclerRequests;
    private ProgressBar progressBar;
    private AdminRequestAdapter adapter;
    private UserService userService;
    private ItemService itemService;
    private RequestService requestService;

    private final List<RequestModel> requestList = new ArrayList<>();
    private final Map<Integer, String> itemNameMap = new HashMap<>();
    private final Map<Integer, String> usernameMap = new HashMap<>();

    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_list);

        recyclerRequests = findViewById(R.id.recyclerRequests);
        progressBar = findViewById(R.id.progressBar);

        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        userService = ApiUtils.getUserService();
        itemService = ApiUtils.getItemService();
        requestService = ApiUtils.getRequestService();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        apiKey = sharedPrefManager.getUser().getToken();

        loadAllData(); // Load items, users, and requests in sequence
    }

    private void loadAllData() {
        progressBar.setVisibility(View.VISIBLE);

        itemService.getRecyclableItems(apiKey).enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (RecyclableItem item : response.body()) {
                        itemNameMap.put(item.getItemId(), item.getItemName()); // ✅ Correct getters
                    }
                    loadUsers();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminRequestListActivity.this, "Failed to fetch items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecyclableItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminRequestListActivity.this, "Item error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers() {
        userService.getAllUsers(apiKey).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (User user : response.body()) {
                        usernameMap.put(user.getId(), user.getUsername());
                    }
                    loadRequests();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminRequestListActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminRequestListActivity.this, "User error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRequests() {
        requestService.getAllRequests(apiKey).enqueue(new Callback<List<RequestModel>>() {
            @Override
            public void onResponse(Call<List<RequestModel>> call, Response<List<RequestModel>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    requestList.clear();
                    requestList.addAll(response.body());

                    adapter = new AdminRequestAdapter(AdminRequestListActivity.this, requestList, itemNameMap, usernameMap);
                    recyclerRequests.setAdapter(adapter);
                } else {
                    Toast.makeText(AdminRequestListActivity.this, "No requests found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RequestModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminRequestListActivity.this, "Request error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
