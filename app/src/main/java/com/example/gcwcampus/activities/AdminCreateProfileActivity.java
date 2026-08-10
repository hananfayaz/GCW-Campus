package com.example.gcwcampus.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.example.gcwcampus.entities.AdminEntity;
import com.example.gcwcampus.models.FirebaseAdminModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class AdminCreateProfileActivity extends AppCompatActivity {

    GcwDatabaseHelper gcwDatabaseHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    boolean isGenderChecked = false, isFormValid = false;
    String name, address, dob, email, password, confirmPassword, genderMale, genderFemale, gender;
    EditText edtTxtName , edtTxtAddress , edtTxtDob , edtTxtEmail , edtTxtPassword , edtTxtConfirmPassword;
    SharedPreferences numberRolePreferences,userNamePref;
    SharedPreferences.Editor userNamePrefEditor;
    RadioGroup rgGender;
    RadioButton rbGenderMale , rbGenderFemale;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_create_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rgGender = findViewById(R.id.rg_gender);
        edtTxtName = findViewById(R.id.edt_txt_name);
        edtTxtAddress = findViewById(R.id.edt_txt_address);
        edtTxtDob = findViewById(R.id.edt_txt_dob);
        edtTxtEmail = findViewById(R.id.edt_txt_email);
        edtTxtPassword = findViewById(R.id.edt_txt_password);
        edtTxtConfirmPassword = findViewById(R.id.edt_txt_confirm_password);
        rbGenderMale = findViewById(R.id.rb_gender_male);
        rbGenderFemale = findViewById(R.id.rb_gender_female);
        btnSubmit = findViewById(R.id.btn_submit);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admin");

        btnSubmit.setEnabled(false);

        edtTxtName.addTextChangedListener(textWatcher);
        edtTxtAddress.addTextChangedListener(textWatcher);
        edtTxtDob.addTextChangedListener(textWatcher);
        edtTxtEmail.addTextChangedListener(textWatcher);
        edtTxtPassword.addTextChangedListener(textWatcher);
        edtTxtConfirmPassword.addTextChangedListener(textWatcher);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtTxtDob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year12, month12, dayOfMonth) -> {
                dob = dayOfMonth + "/" + (month12 +1) + "/" + year12;
                edtTxtDob.setText(dob);
            },year,month,day);
            datePickerDialog.show();
        });

        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.rb_gender_male){
                genderMale = rbGenderMale.getText().toString();
            }else {
                genderFemale = rbGenderFemale.getText().toString();
            }
            isGenderChecked = true;
        });
        
        numberRolePreferences = getSharedPreferences("number",MODE_PRIVATE);
        String phNumber = numberRolePreferences.getString("user_number",null);

        btnSubmit.setOnClickListener(v -> {
            name = edtTxtName.getText().toString();
            address = edtTxtAddress.getText().toString();
            email = edtTxtEmail.getText().toString();
            password = edtTxtPassword.getText().toString();
            confirmPassword = edtTxtConfirmPassword.getText().toString();
            gender = getGender();

            Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");

            if (!password.equals(confirmPassword)){
                Toast.makeText(this, "Wrong confirm password", Toast.LENGTH_SHORT).show();
            } else if (!emailPattern.matcher(email).matches()){
                Toast.makeText(this, "Enter Correct Email", Toast.LENGTH_SHORT).show();
            } else {
                ExecutorService executor = Executors.newFixedThreadPool(2);
                AdminEntity adminEntity = new AdminEntity(name,address,phNumber,dob,email,gender,password);
                executor.submit(() -> {
                    try {
                        gcwDatabaseHelper.adminDao().addAdminInfo(adminEntity);
                    } catch (Exception e){
                        Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                executor.submit(() -> {
                    try {
                        assert phNumber != null;
                        databaseReference.child(phNumber).setValue(new FirebaseAdminModel(name,address,dob,email,gender,password));
                    } catch (Exception e){
                        Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                executor.shutdown();

                userNamePref = getSharedPreferences("check_user_name",MODE_PRIVATE);
                userNamePrefEditor = userNamePref.edit();
                userNamePrefEditor.putString("user_name",name);
                userNamePrefEditor.apply();

                booleanSharedPrefEditor("login","is_login");
                booleanSharedPrefEditor("check_on_profile","is_submitted");

                startActivity(new Intent(AdminCreateProfileActivity.this, DashboardActivity.class));
                finish();
            }
        });
    }
    private void updateFormValidity(){
        isFormValid = !edtTxtName.getText().toString().isEmpty() &&
                !edtTxtAddress.getText().toString().isEmpty() &&
                !edtTxtDob.getText().toString().isEmpty() &&
                !edtTxtEmail.getText().toString().isEmpty() &&
                !edtTxtPassword.getText().toString().isEmpty() &&
                !edtTxtConfirmPassword.getText().toString().isEmpty() &&
                isGenderChecked;
        btnSubmit.setEnabled(isFormValid);
    }
    private void booleanSharedPrefEditor(String preferenceKey, String editorKey){
        SharedPreferences sharedPreference = getSharedPreferences(preferenceKey, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(editorKey,true);
        editor.apply();
    }
    private String getGender() {
        if (rbGenderMale.isChecked()){
            return genderMale;
        } else {
            return genderFemale;
        }
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count==0){
                btnSubmit.setEnabled(false);
            }
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