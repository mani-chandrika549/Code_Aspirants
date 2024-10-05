package com.example.abhaonboarding;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserDetailsActivity extends AppCompatActivity {
    private EditText nameEditText, dobEditText, addressEditText;
    private Button submitDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        nameEditText = findViewById(R.id.et_name);
        dobEditText = findViewById(R.id.et_dob);
        addressEditText = findViewById(R.id.et_address);
        submitDetailsButton = findViewById(R.id.btn_submit_details);

        submitDetailsButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            if (name.isEmpty() || dob.isEmpty() || address.isEmpty()) {
                Toast.makeText(UserDetailsActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Handle form submission (you can save data or call API here)
            Toast.makeText(UserDetailsActivity.this, "Details Submitted", Toast.LENGTH_SHORT).show();

            // Move to next screen
            Intent intent = new Intent(UserDetailsActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
