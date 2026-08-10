package com.example.gcwcampus.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.example.gcwcampus.entities.StudentEntity;
import com.example.gcwcampus.models.FirebaseStudentModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class StudentCreateProfileActivity extends AppCompatActivity {

    boolean isGenderChecked = false, isFormValid = false;
    String name, address, dob, email, password, rollNo, confirmPassword, genderMale, genderFemale, gender, semester, course, regNo;
    EditText edtTxtName, edtTxtAddress, edtTxtDob,edtTxtEmail,edtTxtSem, edtTxtRollNo,edtTxtRegNo,edtTxtPassword,edtTxtConfirmPassword;
    Spinner sprCourse;
    SharedPreferences userNamePref,numberRolePreferences;
    SharedPreferences.Editor userNamePrefEditor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RadioGroup rgGender;
    GcwDatabaseHelper gcwDatabaseHelper;
    RadioButton rbGenderMale, rbGenderFemale;
    Button btnSubmit;
    Pattern emailPattern, regPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_create_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtTxtSem = findViewById(R.id.edt_txt_semester);
        sprCourse = findViewById(R.id.edt_txt_course);
        edtTxtRegNo = findViewById(R.id.edt_txt_reg_no);
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
        edtTxtRollNo = findViewById(R.id.edt_txt_roll_no);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Student");

        btnSubmit.setEnabled(false);

        edtTxtName.addTextChangedListener(textWatcher);
        edtTxtAddress.addTextChangedListener(textWatcher);
        edtTxtDob.addTextChangedListener(textWatcher);
        edtTxtEmail.addTextChangedListener(textWatcher);
        edtTxtPassword.addTextChangedListener(textWatcher);
        edtTxtConfirmPassword.addTextChangedListener(textWatcher);
        edtTxtSem.addTextChangedListener(textWatcher);
        edtTxtRegNo.addTextChangedListener(textWatcher);
        edtTxtRollNo.addTextChangedListener(textWatcher);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtTxtDob.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                dob = dayOfMonth + "/" + (month1 +1) + "/" + year1;
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

        sprCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = parent.getItemAtPosition(position).toString();
                updateFormValidity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(v -> {
            name = edtTxtName.getText().toString();
            address = edtTxtAddress.getText().toString();
            email = edtTxtEmail.getText().toString();
            password = edtTxtPassword.getText().toString();
            confirmPassword = edtTxtConfirmPassword.getText().toString();
            semester = edtTxtSem.getText().toString();
            regNo = edtTxtRegNo.getText().toString();
            rollNo = edtTxtRollNo.getText().toString();
            gender = getGender();

            emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
            regPattern = Pattern.compile("CUS-\\d{2}-SET-\\d{5}");
            
            if (!password.equals(confirmPassword)){
                Toast.makeText(this, "Wrong Confirm Password", Toast.LENGTH_SHORT).show();
            }else if (!emailPattern.matcher(email).matches()){
                Toast.makeText(this, "Enter Correct Email", Toast.LENGTH_SHORT).show();
            } else if (!regPattern.matcher(regNo).matches()){
                Toast.makeText(this, "Enter Correct Reg No.", Toast.LENGTH_SHORT).show();
            } else {
                ExecutorService executor = Executors.newFixedThreadPool(2);
                StudentEntity studentEntity = new StudentEntity(name,address,phNumber,dob,semester,course,rollNo,email,regNo,gender,password);
                executor.submit(() -> {
                    try {
                        gcwDatabaseHelper.studentDao().addStudentInfo(studentEntity);
                    } catch (Exception e){
                        Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                executor.submit(() -> {
                    try {
                        databaseReference.child(course).child(semester).child(rollNo).setValue(new FirebaseStudentModel(name,address,phNumber,dob,email,regNo,gender,password));
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

                startActivity(new Intent(StudentCreateProfileActivity.this, DashboardActivity.class));
                finish();
            }
        });

    }
    private void updateFormValidity() {
        isFormValid = !edtTxtName.getText().toString().isEmpty() && !edtTxtAddress.getText().toString().isEmpty() &&
                !edtTxtDob.getText().toString().isEmpty() && !edtTxtEmail.getText().toString().isEmpty() &&
                !edtTxtPassword.getText().toString().isEmpty() && !edtTxtConfirmPassword.getText().toString().isEmpty() &&
                !edtTxtSem.getText().toString().isEmpty() && !edtTxtRegNo.getText().toString().isEmpty() &&
                !edtTxtRollNo.getText().toString().isEmpty() && isGenderChecked && sprCourse.getSelectedItemPosition() > 0;
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