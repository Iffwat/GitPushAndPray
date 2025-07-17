package com.example.lab_rest;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.example.lab_rest.adapter.AdminRequestAdapter;
import com.example.lab_rest.model.AdminRequestModel;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.*;

import retrofit2.*;

public class AdminRequestListActivity extends AppCompatActivity {

    RecyclerView recyclerAllRequests;
    List<AdminRequestModel> requestList = new ArrayList<>();
    AdminRequestAdapter adapter;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_list);

        recyclerAllRequests = findViewById(R.id.recyclerAllRequests);
        recyclerAllRequests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminRequestAdapter(this, requestList);
        recyclerAllRequests.setAdapter(adapter);

        userService = ApiUtils.getUserService();

        loadAllRequests();
    }

    private void loadAllRequests() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();
        //int user_id = user.getId();
        userService = ApiUtils.getUserService();
        userService.getAllRequests(token).enqueue(new Callback<List<AdminRequestModel>>() {
            @Override
            public void onResponse(Call<List<AdminRequestModel>> call, Response<List<AdminRequestModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requestList.clear();
                    requestList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminRequestListActivity.this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminRequestModel>> call, Throwable t) {
                Toast.makeText(AdminRequestListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
