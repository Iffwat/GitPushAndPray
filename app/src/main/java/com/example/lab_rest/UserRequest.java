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
                    adapter.setCancelClickListener(new RequestAdapter.CancelClickListener() {
                        @Override
                        public void onCancelClicked(int requestId, int position) {
                            cancel_request(requestId, position);
                        }
                    });
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

    // ✅ Moved this outside of loadRequests() / onResponse()
    private void cancel_request(int requestId, int position) {
        userService.cancelRequest(requestId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserRequest.this, "Request cancelled", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemRemoved(position); // or reload list
                } else {
                    Toast.makeText(UserRequest.this, "Cancel failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserRequest.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
