package com.example.gcwcampus.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    String userRole,phNo,email;
    Button btnSendPassword;
    SharedPreferences userRolePref;
    GcwDatabaseHelper gcwDatabaseHelper;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText edtTxtPhnNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTxtPhnNo = findViewById(R.id.editTextPhoneNumber);
        btnSendPassword = findViewById(R.id.btnSendPassword);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);

        edtTxtPhnNo.addTextChangedListener(textWatcher);

        userRolePref = getSharedPreferences("check_user_role",MODE_PRIVATE);
        userRole = userRolePref.getString("user_role",null);

        btnSendPassword.setOnClickListener(v -> {
            phNo = edtTxtPhnNo.getText().toString();
            switch (userRole){
                case "Admin":
                    email = gcwDatabaseHelper.adminDao().getAdminEmail(phNo);
                    sendResetEmail(email);
                    break;
                case "Faculty":
                    email = gcwDatabaseHelper.facultyDao().getFacultyEmail(phNo);
                    sendResetEmail(email);
                    break;
                case "Student":
                    email = gcwDatabaseHelper.studentDao().getStudentEmail(phNo);
                    sendResetEmail(email);
                    break;
                default:
                    Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    private void sendResetEmail(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                edtTxtPhnNo.setText("");
                finish();
            }else {
                Toast.makeText(ForgotPasswordActivity.this, "Error : "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count == 0){
                btnSendPassword.setEnabled(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnSendPassword.setEnabled(s.length() == 10);
        }
    };
}