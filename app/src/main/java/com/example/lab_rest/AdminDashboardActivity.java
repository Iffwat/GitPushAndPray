package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.User;
import com.example.lab_rest.sharedpref.SharedPrefManager;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private ImageView imgProfile; // for logout menu icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminDashboardLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get references
        tvWelcome = findViewById(R.id.tvWelcome);
        imgProfile = findViewById(R.id.imgProfile); // ADD: find the ImageView

        Button btnViewAll = findViewById(R.id.btnViewMyRequest);
        btnViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminRequestListActivity.class);
            startActivity(intent);
        });

        Button btnManageItems = findViewById(R.id.btnManageItems);
        btnManageItems.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ItemManagementActivity.class);
            startActivity(intent);
        });


        // greet the user
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        if (!spm.isLoggedIn()) { // no session record
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            User user = spm.getUser();
            tvWelcome.setText("Hello " + user.getUsername());
        }

        // ADD: Set up the popup menu for logout
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(AdminDashboardActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.admin_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_logout) {
                            logoutClicked(view);
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    public void logoutClicked(View view) {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        Toast.makeText(getApplicationContext(), "You have successfully logged out.", Toast.LENGTH_SHORT).show();

        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
