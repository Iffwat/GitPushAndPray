// File: ItemManagementActivity.java
package com.example.lab_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.ItemAdapter;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.ItemService;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerItems;
    private ItemAdapter adapter;
    private UserService userService;
    private ItemService itemService;
    private List<RecyclableItem> currentItems;
    private int contextMenuPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management);

        recyclerItems = findViewById(R.id.recyclerItems);

        registerForContextMenu(recyclerItems);

        userService = ApiUtils.getUserService();
        itemService = ApiUtils.getItemService();

        loadItems();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        RecyclableItem SItem = adapter.getSelectedItem();
        Log.d("MyApp", "selected "+SItem.toString());    // debug purpose

        if (item.getItemId() == R.id.menu_delete) {
            //user clicked the delete contextual menu
            doDeleteBook(SItem);
        }

        return super.onContextItemSelected(item);
    }

    public void floatingAddBookClicked(View view) {
        // Example: Start AddItemActivity
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }
    private void doDeleteBook(RecyclableItem SItem) {
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // prepare REST API call
        Call<DeleteResponse> call = itemService.deleteRecyclableItem(user.getToken(), SItem.getItemId());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Book successfully deleted");
                    // update data in list view
                    loadItems();
                }
                else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });
    }
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void doUpdateBook(RecyclableItem SItem) {
        Log.d("MyApp:", "updating Item: " + SItem.toString());
        // forward user to UpdateBookActivity, passing the selected book id
        Intent intent = new Intent(getApplicationContext(), ItemFormActivity.class);
        intent.putExtra("item_id", SItem.getItemId());
        startActivity(intent);
    }

    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    private void loadItems() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        itemService.getRecyclableItems(token).enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                Log.d("MyApp", "Response: " + response.raw());
                if (response.code() == 200) {
                    List<RecyclableItem> items = response.body();
                    adapter = new ItemAdapter(getApplicationContext(), items);
                    recyclerItems.setAdapter(adapter);
                    recyclerItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // add separator between item in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerItems.getContext(),
                            DividerItemDecoration.VERTICAL);
                    recyclerItems.addItemDecoration(dividerItemDecoration);
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



    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
}
