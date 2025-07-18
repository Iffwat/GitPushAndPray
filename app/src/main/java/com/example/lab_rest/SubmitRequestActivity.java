package com.example.lab_rest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.ItemService;
import com.example.lab_rest.remote.RequestService;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;
import java.text.SimpleDateFormat;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitRequestActivity extends AppCompatActivity {

    private Spinner spItemType;
    private EditText etAddress, etNotes;
    private TextView tvCreated;
    private Button btnSubmitRequest;
    private ImageView imgProfile;

    private List<Integer> itemIdList;
    private List<String> itemNameList;
    private ArrayAdapter<String> itemAdapter;

    private Date requestDate = new Date(); // Default to today

    private UserService userService;
    private ItemService itemService;
    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_request);

        // Bind views
        spItemType = findViewById(R.id.spItemType);
        etAddress = findViewById(R.id.etAddress);
        etNotes = findViewById(R.id.etNotes);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        tvCreated = findViewById(R.id.tvCreated);
        imgProfile = findViewById(R.id.imgProfile);

        // Init lists & adapter
        itemIdList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemNameList);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spItemType.setAdapter(itemAdapter);

        // Set initial request date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tvCreated.setText(sdf.format(requestDate));

        userService = ApiUtils.getUserService();
        itemService = ApiUtils.getItemService();
        requestService = ApiUtils.getRequestService();

        // Load recyclables
        loadItemsFromDatabase();

        // Submit button logic
        btnSubmitRequest.setOnClickListener(view -> submitRequest());

        // Logout popup on profile icon
        imgProfile.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(SubmitRequestActivity.this, v);
            popup.getMenu().add("Logout").setOnMenuItemClickListener(item -> {
                SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
                spm.logout();
                Toast.makeText(getApplicationContext(), "You have successfully logged out.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SubmitRequestActivity.this, LoginActivity.class));
                finish();
                return true;
            });
            popup.show();
        });
    }

    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        c.setTime(requestDate);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            Calendar pickedDate = new GregorianCalendar(year1, month1, dayOfMonth);
            requestDate = pickedDate.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            tvCreated.setText(sdf.format(requestDate));
        }, year, month, day);

        dpd.show();
    }

    private void loadItemsFromDatabase() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        String token = spm.getUser().getToken();

        itemNameList.clear();
        itemIdList.clear();
        itemNameList.add("Select item...");
        itemIdList.add(-1);

        itemService.getRecyclableItems(token).enqueue(new Callback<List<RecyclableItem>>() {
            @Override
            public void onResponse(Call<List<RecyclableItem>> call, Response<List<RecyclableItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
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

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        int selectedItemId = itemIdList.get(selectedPosition);
        int userId = user.getId();
        String token = user.getToken();

        String requestDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(requestDate);

        requestService.submitRequest(
                token,
                userId,
                selectedItemId,
                address,
                requestDateStr,
                "pending",
                0.0, // default weight
                0.0, // default price
                notes
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SubmitRequestActivity.this, "Request submitted successfully!", Toast.LENGTH_SHORT).show();
                    spItemType.setSelection(0);
                    etAddress.setText("");
                    etNotes.setText("");
                    tvCreated.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                    startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                    finish();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
                    spm.logout();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to submit request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SubmitRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
