package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.lab_rest.adapter.ItemAdapter;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.*;

import retrofit2.*;

public class ItemManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerItems;
    private FloatingActionButton fabAddItem;
    private List<RecyclableItem> itemList = new ArrayList<>();
    private ItemAdapter adapter;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management);

        recyclerItems = findViewById(R.id.recyclerItems);
        fabAddItem = findViewById(R.id.fabAddItem);
        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        userService = ApiUtils.getUserService();

        adapter = new ItemAdapter(this, itemList, new ItemAdapter.ItemActionListener() {
            @Override
            public void onEditClicked(RecyclableItem item) {
                Intent intent = new Intent(ItemManagementActivity.this, ItemFormActivity.class);
                intent.putExtra("itemId", item.getItemId());
                intent.putExtra("itemName", item.getItemName());
                intent.putExtra("pricePerKg", item.getPricePerKg());
                startActivity(intent);
            }

            @Override
            public void onDeleteClicked(int itemId) {
                deleteItem(itemId);
            }
        });

        recyclerItems.setAdapter(adapter);

        fabAddItem.setOnClickListener(v -> {
            // Open form activity to add new item
            startActivity(new Intent(ItemManagementActivity.this, ItemFormActivity.class));
        });

        loadItems();
    }

    private void loadItems() {
        userService.getRecyclableItems().enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemList.clear();
                    itemList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ItemManagementActivity.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecyclableItem>> call, Throwable t) {
                Toast.makeText(ItemManagementActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(int itemId) {
        userService.deleteRecyclableItem(itemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ItemManagementActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    loadItems();
                } else {
                    Toast.makeText(ItemManagementActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ItemManagementActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Optional: refresh the list when returning from form
    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
}
