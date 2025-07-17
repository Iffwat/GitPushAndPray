// File: AdminUpdateActivity.java
package com.example.lab_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUpdateActivity extends AppCompatActivity {

    private TextView tvItemName, tvUserNotes, tvTotalPrice;
    private EditText edtWeight;
    private Spinner spinnerStatus;
    private Button btnUpdateRequest;

    private double pricePerKg = 0.0;
    private int requestId;

    private UserService userService;
    private RequestModel requestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_update);

        Intent intent = getIntent();
        requestId = intent.getIntExtra("requestId", -1);

        tvItemName = findViewById(R.id.tvItemName);
        tvUserNotes = findViewById(R.id.tvNotes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        edtWeight = findViewById(R.id.edtWeight);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdateRequest = findViewById(R.id.btnUpdateRequest);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        userService = ApiUtils.getUserService();

        // Fetch request details
        userService.getRequest(user.getToken(), requestId).enqueue(new Callback<RequestModel>() {
            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requestModel = response.body();
                    tvItemName.setText("Item: " + requestModel.getItemId());
                    tvUserNotes.setText("User Notes: " + requestModel.getNotes());
                    edtWeight.setText(String.valueOf(requestModel.getWeight()));
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

        // Spinner setup
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Pending", "Completed"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Auto-calculate price
        edtWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) calculateTotal();
        });

        // Update button click
        btnUpdateRequest.setOnClickListener(this::updateRequest);
    }

    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void updateRequest(View view) {
        if (requestModel == null) {
            Toast.makeText(this, "Request not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }
        String weightText = edtWeight.getText().toString().trim();
        if (weightText.isEmpty()) {
            Toast.makeText(this, "Weight cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        double weight;
        try {
            weight = Double.parseDouble(weightText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid weight", Toast.LENGTH_SHORT).show();
            return;
        }
        requestModel.setWeight(weight);
        requestModel.setStatus(spinnerStatus.getSelectedItem().toString());

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        Call<RequestModel> call = userService.updateRequest(
                user.getToken(),
                requestModel.getRequestId(),
                requestModel.getUserId(),
                requestModel.getItemId(),
                requestModel.getAddress(),
                requestModel.getRequestDate(),
                requestModel.getStatus(),
                requestModel.getWeight(),
                requestModel.getTotalPrice(),
                requestModel.getNotes()
        );

        call.enqueue(new Callback<RequestModel>() {
            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayUpdateSuccess("Request updated successfully!");
                } else {
                    Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayUpdateSuccess(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), AdminRequestListActivity.class));
                        finish();
                        dialog.cancel();
                    }
                }).show();
    }

    private void calculateTotal() {
        try {
            double weight = Double.parseDouble(edtWeight.getText().toString());
            double total = weight * pricePerKg;
            tvTotalPrice.setText(String.format("Total Price: RM %.2f", total));
        } catch (Exception ignored) {
            tvTotalPrice.setText("Total Price: RM -");
        }
    }
}
