package com.example.abhaonboarding;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPActivity extends AppCompatActivity {
    private EditText otpEditText;
    private Button verifyOtpButton;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpEditText = findViewById(R.id.et_otp);
        verifyOtpButton = findViewById(R.id.btn_verify_otp);
        verificationId = getIntent().getStringExtra("verificationId");

        verifyOtpButton.setOnClickListener(v -> {
            String otp = otpEditText.getText().toString().trim();
            if (otp.isEmpty() || otp.length() < 6) {
                otpEditText.setError("Enter valid OTP");
                otpEditText.requestFocus();
                return;
            }
            verifyCode(otp);
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(OTPActivity.this, UserDetailsActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(OTPActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


