package com.example.lab_rest;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;

import retrofit2.*;

public class ItemFormActivity extends AppCompatActivity {

    EditText edtItemName, edtPricePerKg;
    Button btnSaveItem;

    boolean isEdit = false;
    int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);

        edtItemName = findViewById(R.id.edtItemName);
        edtPricePerKg = findViewById(R.id.edtPricePerKg);
        btnSaveItem = findViewById(R.id.btnSaveItem);

        UserService userService = ApiUtils.getUserService();

        // Check if this is edit mode
        if (getIntent().hasExtra("itemId")) {
            isEdit = true;
            itemId = getIntent().getIntExtra("itemId", -1);
            String name = getIntent().getStringExtra("itemName");
            double price = getIntent().getDoubleExtra("pricePerKg", 0.0);

            edtItemName.setText(name);
            edtPricePerKg.setText(String.valueOf(price));
        }

        btnSaveItem.setOnClickListener(v -> {
            String name = edtItemName.getText().toString().trim();
            String priceStr = edtPricePerKg.getText().toString().trim();

            if (name.isEmpty()) {
                edtItemName.setError("Required");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (Exception e) {
                edtPricePerKg.setError("Invalid price");
                return;
            }

            RecyclableItem item = new RecyclableItem();
            item.setItemName(name);
            item.setPricePerKg(price);

            if (isEdit) {
                item.setItemId(itemId);
                userService.updateRecyclableItem(itemId, item).enqueue(responseCallback("updated"));
            } else {
                userService.addRecyclableItem(item).enqueue(responseCallback("added"));
            }
        });
    }

    private Callback<Void> responseCallback(String action) {
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ItemFormActivity.this, "Item " + action + " successfully", Toast.LENGTH_SHORT).show();
                    finish(); // go back
                } else {
                    Toast.makeText(ItemFormActivity.this, "Failed to " + action + " item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ItemFormActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
