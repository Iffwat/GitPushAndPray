package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AdminUpdateItem extends AppCompatActivity {

    private EditText edtName, edtPricePerKg;




    private RecyclableItem recyclableItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_update_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        int itemId = intent.getIntExtra("itemId", -1);

        edtName = findViewById(R.id.udtItemName);
        edtPricePerKg = findViewById(R.id.udtPricePerKg);


        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        ItemService itemService = ApiUtils.getItemService();

        itemService.getItemById(user.getToken(),itemId).enqueue(new Callback<RecyclableItem>() {
            @Override
            public void onResponse(Call<RecyclableItem> call, Response<RecyclableItem> response) {
                Log.d("MyApp:", "Update Form Populate Response: " + response.raw().toString());

                if(response.code() == 200){
                    recyclableItem = response.body();

                    edtName.setText(recyclableItem.getItemName());
                    edtPricePerKg.setText(String.valueOf(recyclableItem.getPricePerKg()));

                }else{

                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<RecyclableItem> call, Throwable throwable) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();

            }
        });



       }

    public void updateItem(View view) {
        String updatedName = edtName.getText().toString().trim();
        double updatedPrice = Double.parseDouble(edtPricePerKg.getText().toString().trim());


        recyclableItem.setItemName(updatedName);
        recyclableItem.setPricePerKg(updatedPrice);

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        ItemService itemService = ApiUtils.getItemService();
        Call<RecyclableItem> call = itemService.updateRecyclableItem(
                user.getToken(),
                recyclableItem.getItemId(),
                recyclableItem.getItemName(),
                recyclableItem.getPricePerKg()
        );

        call.enqueue((new Callback<RecyclableItem>() {
            @Override
            public void onResponse(Call<RecyclableItem> call, Response<RecyclableItem> response) {
                if(response.isSuccessful()){
                    recyclableItem = response.body();
                    Toast.makeText(AdminUpdateItem.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AdminUpdateItem.this, "Update item Failed, Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecyclableItem> call, Throwable throwable) {
                Toast.makeText(AdminUpdateItem.this, "Error :" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}







