package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.UpdateRequestModel;
import com.example.lab_rest.remote.ApiUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUpdateActivity extends AppCompatActivity {

    TextView tvItemName, tvUserNotes, tvTotalPrice;
    EditText edtWeight;
    Spinner spinnerStatus;
    Button btnUpdateRequest;
    double pricePerKg = 0.0;
    int requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        tvItemName = findViewById(R.id.tvItemName);
        tvUserNotes = findViewById(R.id.tvNotes);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        edtWeight = findViewById(R.id.edtWeight);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnUpdateRequest = findViewById(R.id.btnUpdateRequest);

        // Spinner setup
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Pending", "Completed"});
        spinnerStatus.setAdapter(statusAdapter);

        // Get Intent extras
        Intent i = getIntent();
        requestId = i.getIntExtra("requestId", -1);
        String itemName = i.getStringExtra("itemName");
        String notes = i.getStringExtra("notes");
        pricePerKg = i.getDoubleExtra("pricePerKg", 0.0);

        tvItemName.setText("Item: " + itemName);
        tvUserNotes.setText("User Notes: " + notes);

        // Auto-calculate price
        edtWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) calculateTotal();
        });

        btnUpdateRequest.setOnClickListener(v -> {
            double weight;
            try {
                weight = Double.parseDouble(edtWeight.getText().toString());
            } catch (Exception e) {
                edtWeight.setError("Invalid weight");
                return;
            }

            String status = spinnerStatus.getSelectedItem().toString();
            UpdateRequestModel model = new UpdateRequestModel(requestId, weight, status);

            ApiUtils.getUserService().updateRequest(model).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful() && "success".equals(response.body().get("status"))) {
                        Toast.makeText(AdminUpdateActivity.this, "Request updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminUpdateActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(AdminUpdateActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
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