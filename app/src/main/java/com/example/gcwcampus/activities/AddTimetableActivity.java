package com.example.gcwcampus.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.example.gcwcampus.entities.TimetableEntity;
import com.example.gcwcampus.models.FirebaseTimetableModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTimetableActivity extends AppCompatActivity {

    boolean isFormValid = false;
    String sem, department, subject, startTime, endTime, period, faculty, day ;
    Spinner spnSem, spnPeriods, spnFaculties, spnDepartment, spnDays;
    GcwDatabaseHelper databaseHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference facultyReference, timetableReference;
    EditText edtTxtSubject;
    TimePicker timePickerStart, timePickerEnd;
    Button btnAddTimetable;
    FirebaseTimetableModel firebaseTimetableModel = new FirebaseTimetableModel();
    ArrayList<String> faculties = new ArrayList<>();
    ArrayAdapter<String> arrAdapter,arrFacultyAdapter, arrDepartmentAdapter;
    GcwDatabaseHelper gcwDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_timetable);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spnDays = findViewById(R.id.spn_days);
        spnSem = findViewById(R.id.spn_sem);
        spnPeriods = findViewById(R.id.spn_period);
        edtTxtSubject = findViewById(R.id.edt_txt_subject);
        timePickerStart = findViewById(R.id.tp_start_time);
        timePickerEnd = findViewById(R.id.tp_end_time);
        spnFaculties = findViewById(R.id.spn_faculty);
        spnDepartment = findViewById(R.id.spn_department);
        btnAddTimetable = findViewById(R.id.btn_add_timetable);

        btnAddTimetable.setEnabled(false);
        edtTxtSubject.addTextChangedListener(textWatcher);
        timePickerStart.setIs24HourView(false);
        timePickerEnd.setIs24HourView(false);
        spnFaculties.setEnabled(false);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);

        databaseHelper = GcwDatabaseHelper.getDatabase(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        timetableReference = firebaseDatabase.getReference("Timetable");
        facultyReference = firebaseDatabase.getReference("Added Faculty");

        arrDepartmentAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.departments));
        arrDepartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDepartment.setAdapter(arrDepartmentAdapter);

        arrFacultyAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,faculties);
        arrFacultyAdapter.insert("--Select--",0);
        arrFacultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFaculties.setAdapter(arrFacultyAdapter);

        arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.semesters));
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSem.setAdapter(arrAdapter);
        spnPeriods.setAdapter(arrAdapter);

        spinnerSelection(spnDays);
        spinnerSelection(spnSem);
        spinnerSelection(spnPeriods);
        spinnerSelection(spnFaculties);

        spnDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItemPosition() > 0) {
                        faculties.clear();
                        faculties.add(0,"--Select--");
                        if (spnSem.getSelectedItemPosition() == 0){
                            parent.setSelection(0);
                            Toast.makeText(AddTimetableActivity.this, "Select semester first", Toast.LENGTH_LONG).show();
                        } else {
                            department = parent.getItemAtPosition(position).toString();
                            updateFormValidity();
                            facultyReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    DataSnapshot childSnapshot = snapshot.child(department);
                                    if (childSnapshot.hasChild(sem)){
                                        for (DataSnapshot facultiesSnapshot : snapshot.child(department).child(sem).getChildren()) {
                                            for (DataSnapshot facultySnapshot : facultiesSnapshot.getChildren()){
                                                faculties.add(facultySnapshot.getValue(String.class));
                                            }
                                            arrFacultyAdapter.notifyDataSetChanged();
                                        }
                                        spnFaculties.setEnabled(true);
                                    } else {
                                        Toast.makeText(AddTimetableActivity.this, "Faculty Not Found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AddTimetableActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        timePickerStart.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            startTime = hourOfDay + ":" + minute;
            updateFormValidity();
        });
        timePickerEnd.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            endTime = hourOfDay + ":" + minute;
            updateFormValidity();
        });

        btnAddTimetable.setOnClickListener(v -> {
            if (isFormValid) {
                subject = edtTxtSubject.getText().toString();
                ExecutorService executor = Executors.newFixedThreadPool(2);
                TimetableEntity timetableEntity = new TimetableEntity(sem, subject, startTime, endTime, faculty, department, period,day);
                executor.submit(() -> databaseHelper.timetableDao().addTimetable(timetableEntity));
                executor.submit(() -> {
                    timetableReference.child(department).child(sem).child(day).child(period).child("subject details").setValue(new FirebaseTimetableModel(subject,startTime,endTime,faculty)).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Timetable added successfully", Toast.LENGTH_SHORT).show();
                            firebaseTimetableModel.setDay(day);
                        }
                    }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
                });
                executor.shutdown();
                finish();
            }
        });

    }
    private void updateFormValidity() {
        isFormValid = edtTxtSubject.getText().length() > 0 &&
                spnSem.getSelectedItemPosition() > 0 &&
                spnPeriods.getSelectedItemPosition() > 0 &&
                spnFaculties.getSelectedItemPosition() > 0 &&
                spnDepartment.getSelectedItemPosition() > 0 &&
                spnDays.getSelectedItemPosition() > 0 &&
                startTime != null &&
                endTime != null;
        btnAddTimetable.setEnabled(isFormValid);
    }
    private void spinnerSelection(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.equals(spnDays)) {
                    day = parent.getItemAtPosition(position).toString();
                    updateFormValidity();
                } else if (spinner.equals(spnSem)) {
                    sem = parent.getItemAtPosition(position).toString();
                    updateFormValidity();
                } else if (spinner.equals(spnPeriods)) {
                    period = parent.getItemAtPosition(position).toString();
                    updateFormValidity();
                } else if (spinner.equals(spnFaculties)) {
                    faculty = parent.getItemAtPosition(position).toString();
                    updateFormValidity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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