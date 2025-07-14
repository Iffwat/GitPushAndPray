package com.example.lab_rest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.RequestAdapter;
import com.example.lab_rest.model.RequestHistoryModel;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRequest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        recyclerView = findViewById(R.id.recyclerRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userService = ApiUtils.getUserService();

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Session expired. Please log in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRequests(userId);
    }

    private void loadRequests(int userId) {
        userService.getUserRequests(userId).enqueue(new Callback<List<RequestHistoryModel>>() {
            @Override
            public void onResponse(Call<List<RequestHistoryModel>> call, Response<List<RequestHistoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new RequestAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(UserRequest.this, "No requests found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RequestHistoryModel>> call, Throwable t) {
                Toast.makeText(UserRequest.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
