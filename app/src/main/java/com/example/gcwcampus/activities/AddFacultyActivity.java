package com.example.gcwcampus.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFacultyActivity extends AppCompatActivity {

    boolean isFormValid = false;
    String dep, sem, facultyName;
    Spinner spnDep, spnSem;
    EditText faculty;
    Button btnSubmit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_faculty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spnDep = findViewById(R.id.spn_faculty_dep);
        spnSem = findViewById(R.id.spn_faculty_sem);
        faculty = findViewById(R.id.faculty_name);
        btnSubmit = findViewById(R.id.btn_add_faculty);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Added Faculty");

        btnSubmit.setEnabled(false);

        setSpinnerListener(spnDep);
        setSpinnerListener(spnSem);
        faculty.addTextChangedListener(textWatcher);

        btnSubmit.setOnClickListener(v -> {
            facultyName = faculty.getText().toString();
            databaseReference.child(dep).child(sem).child("Faculties").push().setValue(facultyName).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()){
                    Toast.makeText(AddFacultyActivity.this, "Faculty Added Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        });

    }
    private void setSpinnerListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner == spnDep){
                    dep = parent.getItemAtPosition(position).toString();
                } else if (spinner == spnSem) {
                    sem = parent.getItemAtPosition(position).toString();
                }
                updateFormValidity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void updateFormValidity(){
        isFormValid = spnDep.getSelectedItemPosition() > 0
                && spnSem.getSelectedItemPosition() > 0
                && !faculty.getText().toString().isEmpty();
        btnSubmit.setEnabled(isFormValid);
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