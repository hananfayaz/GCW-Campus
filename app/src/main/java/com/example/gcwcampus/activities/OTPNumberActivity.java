package com.example.gcwcampus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;

public class OTPNumberActivity extends AppCompatActivity {

    boolean isFormValid = false;
    String userRole;
    Spinner sprUserRoles;
    EditText edtTxtPhnNo;
    Button btnSendOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_number);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTxtPhnNo = findViewById(R.id.edt_txt_phone_number);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        sprUserRoles = findViewById(R.id.spr_user_roles);

        btnSendOtp.setEnabled(false);
        edtTxtPhnNo.addTextChangedListener(textWatcher);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.user_roles));
        sprUserRoles.setAdapter(spinnerAdapter);

        sprUserRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    userRole = sprUserRoles.getItemAtPosition(position).toString();
                    SharedPreferences userRolePreferences = getSharedPreferences("check_user_role",MODE_PRIVATE);
                    SharedPreferences.Editor userRolePrefEditor = userRolePreferences.edit();
                    userRolePrefEditor.putString("user_role",userRole);
                    userRolePrefEditor.apply();
                }
                updateFormValidity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
        btnSendOtp.setOnClickListener(v -> {
            if (edtTxtPhnNo.getText().toString().length()!=10){
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
            } else if (sprUserRoles.getSelectedItemPosition()==0) {
                Toast.makeText(this, "Choose the role", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefNumber = getSharedPreferences("number",MODE_PRIVATE);
                SharedPreferences.Editor prefNumberEditor = prefNumber.edit();
                prefNumberEditor.putString("user_number",edtTxtPhnNo.getText().toString());
                prefNumberEditor.apply();
                Intent intentVerifyOtpActivity = new Intent(OTPNumberActivity.this, VerifyOTPActivity.class);
                intentVerifyOtpActivity.putExtra("phone_number","+91".concat(edtTxtPhnNo.getText().toString()));
                intentVerifyOtpActivity.putExtra("user_role",userRole);
                startActivity(intentVerifyOtpActivity);
                finish();
            }
        });
    }
    private void updateFormValidity(){
        isFormValid = sprUserRoles.getSelectedItemPosition() > 0
                && edtTxtPhnNo.getText().length() == 10;
        btnSendOtp.setEnabled(isFormValid);
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateFormValidity();
        }
    };
}