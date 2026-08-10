package com.example.gcwcampus.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gcwcampus.R;
import com.example.gcwcampus.adapters.RecyclerFacultiesAdapter;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FacultiesActivity extends AppCompatActivity {

    String studentDepartment, studentSem, studentRollNo, phNo;
    TextView tvDepartment, tvSem;
    RecyclerView rvFaculty;
    ArrayList<String> faculties = new ArrayList<>();
    RecyclerFacultiesAdapter facultiesAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference addedFacultyDbRef;
    GcwDatabaseHelper gcwDatabaseHelper;
    SharedPreferences numberPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculties);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvDepartment = findViewById(R.id.tv_dep_faculty);
        tvSem = findViewById(R.id.tv_sem_faculty);
        rvFaculty = findViewById(R.id.rv_faculty);

        rvFaculty.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        firebaseDatabase = FirebaseDatabase.getInstance();
        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);

        numberPref = getSharedPreferences("number",MODE_PRIVATE);
        phNo = numberPref.getString("user_number",null);

        studentDepartment = gcwDatabaseHelper.studentDao().getStudentCourse(phNo);
        studentSem = gcwDatabaseHelper.studentDao().getStudentSem(phNo);
        studentRollNo = gcwDatabaseHelper.studentDao().getRollNo(phNo);

        addedFacultyDbRef = firebaseDatabase.getReference("Added Faculty").child(studentDepartment).child(studentSem).child("Faculties");

        tvDepartment.setText(studentDepartment);
        tvSem.setText(studentSem);

        addedFacultyDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    faculties.add(dataSnapshot.getValue(String.class));
                }
                facultiesAdapter.notifyItemRangeInserted(facultiesAdapter.getItemCount(), faculties.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultiesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        facultiesAdapter = new RecyclerFacultiesAdapter(this,faculties);
        rvFaculty.setAdapter(facultiesAdapter);

    }
}