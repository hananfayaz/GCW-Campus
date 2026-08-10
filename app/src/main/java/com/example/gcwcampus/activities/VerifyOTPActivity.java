package com.example.gcwcampus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText edtTxtEnterOtp;
    Button btnVerify;
    TextView txtViewResendOtp;
    String phoneNumber , userRole;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    PhoneAuthCredential credential;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTxtEnterOtp = findViewById(R.id.edt_txt_enter_otp);
        btnVerify = findViewById(R.id.btn_verify);
        txtViewResendOtp = findViewById(R.id.txt_view_resend_otp);
        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();

        phoneNumber = getIntent().getStringExtra("phone_number");
        userRole = getIntent().getStringExtra("user_role");
        btnVerify.setEnabled(false);
        edtTxtEnterOtp.addTextChangedListener(textWatcher);
        sendOtp();
        btnVerify.setOnClickListener(v -> {
            if (edtTxtEnterOtp.getText().toString().length()!=6) {
                Toast.makeText(VerifyOTPActivity.this, "Enter a valid OTP", Toast.LENGTH_SHORT).show();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                btnVerify.setEnabled(false);
                credential = PhoneAuthProvider.getCredential(verificationCode, edtTxtEnterOtp.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
        txtViewResendOtp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            resendOtp();
            Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
        });
    }
    private void sendOtp() {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                phoneAuthCredential.getSmsCode();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationCode = s;
                resendingToken = forceResendingToken;
            }


        }).build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }
    private void resendOtp(){
        PhoneAuthOptions resendAuthOptions = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationCode = s;
                resendingToken = forceResendingToken;
            }

        }).setForceResendingToken(resendingToken).build();
        PhoneAuthProvider.verifyPhoneNumber(resendAuthOptions);
        progressBar.setVisibility(View.GONE);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                SharedPreferences otpVerifiedPref = getSharedPreferences("otp",MODE_PRIVATE);
                SharedPreferences.Editor otpVerPrefEditor = otpVerifiedPref.edit();
                otpVerPrefEditor.putBoolean("is_otp_verified",true);
                otpVerPrefEditor.apply();
                if (Objects.equals(userRole, "Admin")){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(VerifyOTPActivity.this, AdminCreateProfileActivity.class));
                    finish();
                } else if (Objects.equals(userRole,"Faculty")) {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(VerifyOTPActivity.this, FacultyCreateProfileActivity.class));
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(VerifyOTPActivity.this, StudentCreateProfileActivity.class));
                    finish();
                }
            } else if (!edtTxtEnterOtp.getText().toString().equals(credential.toString())){
                Toast.makeText(this, "Enter a valid OTP", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(VerifyOTPActivity.this, "OTP Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count==0){
                btnVerify.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count>0){
                btnVerify.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().isEmpty()){
                btnVerify.setEnabled(false);
            }
        }
    };

}