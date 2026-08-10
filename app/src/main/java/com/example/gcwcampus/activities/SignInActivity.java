package com.example.gcwcampus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;

public class SignInActivity extends AppCompatActivity {

    private int adminId,facultyId,studentId;
    String phNo,password,number,pass,userRole;
    SharedPreferences userRolePref,loginPref;
    SharedPreferences.Editor loginPrefEditor;
    EditText edtTxtUsername,edtTxtPassword;
    TextView forgotPassword;
    Button btnSignIn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTxtUsername = findViewById(R.id.edt_txt_phone_number);
        edtTxtPassword = findViewById(R.id.edit_text_password);
        forgotPassword = findViewById(R.id.txt_forgot_password);
        btnSignIn = findViewById(R.id.sign_in_btn);
        
        btnSignIn.setEnabled(false);
        
        edtTxtUsername.addTextChangedListener(textWatcher);
        edtTxtPassword.addTextChangedListener(textWatcher);

        userRolePref = getSharedPreferences("check_user_role",MODE_PRIVATE);
        userRole = userRolePref.getString("user_role",null);
        loginPref = getSharedPreferences("login",MODE_PRIVATE);
        GcwDatabaseHelper gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        loginPrefEditor = loginPref.edit();
        loginPrefEditor.putBoolean("is_login",false);
        loginPrefEditor.apply();



        btnSignIn.setOnClickListener(v -> {
            loginPrefEditor = loginPref.edit();
            loginPrefEditor.putBoolean("is_login",true);
            loginPrefEditor.apply();

            phNo = edtTxtUsername.getText().toString();
            password = edtTxtPassword.getText().toString();
            assert userRole != null;
            switch (userRole) {
                case "Admin":
                    adminId = gcwDatabaseHelper.adminDao().getAdminId(phNo);
                    number = gcwDatabaseHelper.adminDao().getAdminPhNo(adminId);
                    pass = gcwDatabaseHelper.adminDao().getAdminPassword(phNo);
                    break;
                case "Faculty":
                    facultyId = gcwDatabaseHelper.facultyDao().getFacultyId(phNo);
                    number = gcwDatabaseHelper.facultyDao().getFacultyPhNo(facultyId);
                    pass = gcwDatabaseHelper.facultyDao().getFacultyPassword(phNo);
                    break;
                case "Student":
                    studentId = gcwDatabaseHelper.studentDao().getStudentId(phNo);
                    number = gcwDatabaseHelper.studentDao().getStudentPhNo(studentId);
                    pass = gcwDatabaseHelper.studentDao().getStudentPassword(phNo);
                    break;
                default:
                    Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show();
                    break;
            }
            
            if (phNo.equals(number) && password.equals(pass) ){
                startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                finish();
            } else if (!phNo.equals(number)){
                Toast.makeText(this, "Enter correct username", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter correct password", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count==0){
                btnSignIn.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnSignIn.setEnabled(!edtTxtUsername.getText().toString().isEmpty() && !edtTxtPassword.getText().toString().isEmpty());
        }
    };
}