package com.example.abhaonboarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText phoneEditText, otpEditText;
    private Button sendOtpButton, verifyOtpButton;
    private String verificationId;
    private LinearLayout phoneLayout, otpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        phoneEditText = findViewById(R.id.et_phone_number);
        otpEditText = findViewById(R.id.et_otp);
        sendOtpButton = findViewById(R.id.btn_send_otp);
        verifyOtpButton = findViewById(R.id.btn_verify_otp);

        phoneLayout = findViewById(R.id.phone_layout); // The layout containing phone number input
        otpLayout = findViewById(R.id.otp_layout);     // The layout containing OTP input

        // Initially show phone number layout and hide OTP layout
        otpLayout.setVisibility(View.GONE);

        // Send OTP button listener
        sendOtpButton.setOnClickListener(v -> {
            String phoneNumber = phoneEditText.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(MainActivity.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            sendVerificationCode(phoneNumber);  // Send OTP
        });

        // Verify OTP button listener
        verifyOtpButton.setOnClickListener(v -> {
            String otpCode = otpEditText.getText().toString().trim();

            if (TextUtils.isEmpty(otpCode)) {
                Toast.makeText(MainActivity.this, "Enter the OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            verifyCode(otpCode);  // Verify the OTP
        });
    }

    // Method to send the OTP to the user's phone number
    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)              // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)        // Timeout duration
                .setActivity(this)                        // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Auto-verification happens (skip manual OTP entry)
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // OTP sending failed, display a message to the user
                        Toast.makeText(MainActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // OTP sent, proceed with OTP verification
                        MainActivity.this.verificationId = verificationId;
                        Toast.makeText(MainActivity.this, "OTP sent successfully.", Toast.LENGTH_SHORT).show();

                        // Show OTP input layout
                        phoneLayout.setVisibility(View.GONE);
                        otpLayout.setVisibility(View.VISIBLE);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    // Method to verify the OTP
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);  // Sign in the user
    }

    // Method to sign in the user using PhoneAuthCredential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign-in success
                Toast.makeText(MainActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Sign-in failed
                Toast.makeText(MainActivity.this, "Invalid OTP, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
