package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.ItemService;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etPricePerKg;
    private Button btnAddItem;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etPricePerKg = findViewById(R.id.etPricePerKg);
        btnAddItem = findViewById(R.id.btnAddItem);

        userService = ApiUtils.getUserService();

        btnAddItem.setOnClickListener(view -> addItem());
    }

    private void addItem() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("itemid",-1);
        String itemName = etItemName.getText().toString().trim();
        String priceStr = etPricePerKg.getText().toString().trim();

        if (itemName.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double pricePerKg;
        try {
            pricePerKg = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();
        //int user_id = user.getId();
        ItemService recyclableItem = ApiUtils.getItemService();

        Call <RecyclableItem> call = recyclableItem.addRecyclableItem(token, itemName, pricePerKg);

        call.enqueue(new Callback<RecyclableItem>() {
            @Override
            public void onResponse(Call<RecyclableItem> call, Response<RecyclableItem> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    etItemName.setText("");
                    etPricePerKg.setText("");
                } else {
                    Toast.makeText(AddItemActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecyclableItem> call, Throwable t) {
                Toast.makeText(AddItemActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }

}