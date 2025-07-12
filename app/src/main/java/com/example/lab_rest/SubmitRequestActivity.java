package com.example.lab_rest;
// TODO: 🔁 Change this to your actual package name if needed

import android.content.SharedPreferences;
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
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class SubmitRequestActivity extends AppCompatActivity {

    private Spinner spItemType;
    private EditText etAddress, etNotes;
    private Button btnSubmitRequest;

    private ArrayList<String> itemNameList = new ArrayList<>();
    private ArrayList<Integer> itemIdList = new ArrayList<>();
    private ArrayAdapter<String> itemAdapter;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_submit_request);
        // TODO: 🔁 Make sure your layout file is named correctly and includes the IDs used below

        // Bind UI elements
        spItemType = findViewById(R.id.spItemType);
        etAddress = findViewById(R.id.etAddress);
        etNotes = findViewById(R.id.etNotes);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        // Spinner setup
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemNameList);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItemType.setAdapter(itemAdapter);

        // Retrofit service
        userService = ApiUtils.getUserService();

        // Load recyclable items
        loadItemsFromDatabase();

        // Handle button click
        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest();
            }
        });
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

        if (selectedPosition < 0 || selectedPosition >= itemIdList.size()) {
            Toast.makeText(this, "Invalid item selection", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedItemId = itemIdList.get(selectedPosition);
        String selectedItemName = itemNameList.get(selectedPosition);

        // TODO: 🔁 Replace this with real user ID from login session (e.g., SharedPreferences)
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);  // default -1 if not found

        RequestModel request = new RequestModel(userId, selectedItemId, address, notes);

        // Send POST request
        userService.submitRequest(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SubmitRequestActivity.this, "Request submitted successfully!", Toast.LENGTH_SHORT).show();

                    // Clear form
                    spItemType.setSelection(0);
                    etAddress.setText("");
                    etNotes.setText("");
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
        Call<List<RecyclableItem>> call = userService.getRecyclableItems();

        call.enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemNameList.clear();
                    itemIdList.clear();
                    for (RecyclableItem item : response.body()) {
                        itemIdList.add(item.getItemId());
                        itemNameList.add(item.getItemName());
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
