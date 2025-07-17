// File: SubmitRequestActivity.java
package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubmitRequestActivity extends AppCompatActivity {

    private Spinner spItemType;
    private EditText etAddress, etNotes;
    private Button btnSubmitRequest;

    private List<Integer> itemIdList;
    private List<String> itemNameList;
    private ArrayAdapter<String> itemAdapter;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_request);

        spItemType = findViewById(R.id.spItemType);
        etAddress = findViewById(R.id.etAddress);
        etNotes = findViewById(R.id.etNotes);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        itemIdList = new ArrayList<>();
        itemNameList = new ArrayList<>();

        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemNameList);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItemType.setAdapter(itemAdapter);

        userService = ApiUtils.getUserService();

        loadItemsFromDatabase();

        btnSubmitRequest.setOnClickListener(view -> submitRequest());
    }

    private void submitRequest() {
        String address = etAddress.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        int selectedPosition = spItemType.getSelectedItemPosition();

        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return;
        }

        if (selectedPosition <= 0 || selectedPosition >= itemIdList.size()) {
            Toast.makeText(this, "Please select a valid item", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedItemId = itemIdList.get(selectedPosition);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();
        int userId = user.getId();

        String requestDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String status = "pending";
        double weight = 0.0; // placeholder
        double totalPrice = 0.0; // placeholder


        userService.submitRequest(
                token,
                userId,
                selectedItemId,
                address,
                requestDate,
                status,
                weight,
                totalPrice,
                notes
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SubmitRequestActivity.this, "Request submitted successfully!", Toast.LENGTH_SHORT).show();
                    spItemType.setSelection(0);
                    etAddress.setText("");
                    etNotes.setText("");

                    Intent intent = new Intent(getApplicationContext(), UserDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SubmitRequestActivity.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SubmitRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadItemsFromDatabase() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        itemNameList.clear();
        itemIdList.clear();
        itemNameList.add("Select item...");
        itemIdList.add(-1);

        userService.getRecyclableItems(token).enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (RecyclableItem item : response.body()) {
                        if (item.getItemName() != null) {
                            itemIdList.add(item.getItemId());
                            itemNameList.add(item.getItemName());
                        }
                    }
                    itemAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SubmitRequestActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecyclableItem>> call, Throwable t) {
                Toast.makeText(SubmitRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
