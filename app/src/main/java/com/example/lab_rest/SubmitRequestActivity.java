package com.example.lab_rest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SubmitRequestActivity extends AppCompatActivity {

    private Spinner spItemType;
    private EditText etAddress, etNotes;
    private Button btnSubmitRequest;

    // TODO: You can change this list or fetch dynamically from your database (via REST API)
    private String[] itemTypes = {"Paper", "Plastic", "Glass", "Metal", "Electronics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Make sure this matches your XML file name (e.g., R.layout.activity_submit_request)
        setContentView(R.layout.activity_submit_request);

        // Linking UI elements
        spItemType = findViewById(R.id.spItemType);   // Must match ID in XML
        etAddress = findViewById(R.id.etAddress);     // Must match ID in XML
        etNotes = findViewById(R.id.etNotes);         // Must match ID in XML
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest); // Must match ID in XML

        // Set Spinner values (temporary: static)
        // TODO: Replace with dynamic values from backend if needed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemTypes);
        spItemType.setAdapter(adapter);

        // Submit button click listener
        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRequest(); // Calls method to process form
            }
        });
    }

    private void submitRequest() {
        // Get user input
        String selectedItem = spItemType.getSelectedItem().toString();
        String address = etAddress.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        // Validate required input
        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return;
        }

        // TODO: Replace this with your actual API call to backend (MySQL via pRESTige)
        // Example:
        // ApiClient.postRequest(userId, selectedItem, address, notes);

        // Temporary feedback for testing
        Toast.makeText(this, "Request submitted:\nItem: " + selectedItem + "\nAddress: " + address, Toast.LENGTH_LONG).show();

        // TODO: Optionally clear fields after submission
        spItemType.setSelection(0);
        etAddress.setText("");
        etNotes.setText("");
    }
}
