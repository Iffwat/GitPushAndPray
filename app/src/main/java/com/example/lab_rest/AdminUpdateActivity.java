package com.example.lab_rest;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.RequestService;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUpdateActivity extends AppCompatActivity {

    private TextView tvItemName, tvNotes, tvTotalPrice;
    private EditText edtWeight;
    private Spinner spinnerStatus;
    private Button btnUpdateRequest;

    private int requestId, itemId, userId;
    private String currentStatus, notes, address;
    private double pricePerKg, weightValue = 0.0, totalPrice = 0.0;

    private UserService userService;
    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        // Initialize views
        tvItemName = findViewById(R.id.tvItemName);
        tvNotes = findViewById(R.id.tvNotes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        edtWeight = findViewById(R.id.edtWeight);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdateRequest = findViewById(R.id.btnUpdateRequest);

        // Get Intent data
        requestId = getIntent().getIntExtra("request_id", -1);
        itemId = getIntent().getIntExtra("item_id", -1);
        userId = getIntent().getIntExtra("user_id", -1);
        currentStatus = getIntent().getStringExtra("status");
        notes = getIntent().getStringExtra("notes");
        address = getIntent().getStringExtra("address");
        pricePerKg = getIntent().getDoubleExtra("price_per_kg", 0.0);

        // Display data
        tvItemName.setText("Item ID: " + itemId);
        tvNotes.setText("User Notes: " + (notes == null ? "-" : notes));
        // Status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        if (currentStatus != null) {
            int position = adapter.getPosition(currentStatus);
            spinnerStatus.setSelection(position);
        }

        // Auto-calculate price when user leaves weight field
        edtWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) calculateTotalPrice();
        });

        // Update button
        btnUpdateRequest.setOnClickListener(v -> {
            updateRequest();
        });

        userService = ApiUtils.getUserService();
        requestService = ApiUtils.getRequestService();
    }

    private void calculateTotalPrice() {
        String weightStr = edtWeight.getText().toString().trim();
        if (!weightStr.isEmpty()) {
            try {
                weightValue = Double.parseDouble(weightStr);
                double total = weightValue * pricePerKg;
                tvTotalPrice.setText("Total Price: RM " + String.format("%.2f", total));
            } catch (NumberFormatException e) {
                tvTotalPrice.setText("Total Price: RM -");
                weightValue = 0.0;
            }
        } else {
            tvTotalPrice.setText("Total Price: RM -");
        }
    }

    private void updateRequest() {
        String apiKey = new SharedPrefManager(this).getUser().getToken();
        String status = spinnerStatus.getSelectedItem().toString();

        // ✅ Recalculate weight and total price safely here
        String weightStr = edtWeight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            Toast.makeText(this, "Please enter weight.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            weightValue = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid weight value.", Toast.LENGTH_SHORT).show();
            return;
        }

        totalPrice = weightValue * pricePerKg;

        Call<RequestModel> call = requestService.updateRequest(
                apiKey,
                requestId,
                userId,
                itemId,
                address,
                status,
                weightValue,
                totalPrice,
                notes
        );

        call.enqueue(new Callback<RequestModel>() {
            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminUpdateActivity.this, "Request updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminUpdateActivity.this, "Update failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable t) {
                Toast.makeText(AdminUpdateActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
