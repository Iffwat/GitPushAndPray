package com.example.lab_rest;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.RequestAdapter;
import com.example.lab_rest.model.RequestHistoryModel;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerRequests;
    private RequestAdapter requestAdapter;
    private List<RequestHistoryModel> requestList;
    private UserService userService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        recyclerRequests = findViewById(R.id.recyclerRequests);
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(requestList);
        recyclerRequests.setAdapter(requestAdapter);

        userService = ApiUtils.getUserService();
        String token = new SharedPrefManager(this).getUser().getToken();

        // Load requests
        loadMyRequests(token);

        // Handle cancel click
        requestAdapter.setCancelClickListener((requestId, position) -> {
            userService.cancelRequest(token,requestId).enqueue(new Callback<Void>() {
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
        userService.getUserRequests(token).enqueue(new Callback<List<RequestHistoryModel>>() { //error getMyRequest()
            @Override
            public void onResponse(Call<List<RequestHistoryModel>> call, Response<List<RequestHistoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requestList.clear();
                    requestList.addAll(response.body());
                    requestAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ViewRequestActivity.this, "No requests found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RequestHistoryModel>> call, Throwable t) {
                Toast.makeText(ViewRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
