package com.example.gcwcampus.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.gcwcampus.adapters.RecyclerFacultyStudentAdapter;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FacultyStudentActivity extends AppCompatActivity {

    String dep,name,number;
    ArrayList<String> studentList = new ArrayList<>();
    ArrayList<String> sem = new ArrayList<>();
    RecyclerView rvStudent;
    RecyclerFacultyStudentAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference semReference,studentReference;
    GcwDatabaseHelper gcwDatabaseHelper;
    SharedPreferences numberPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faculty_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rvStudent = findViewById(R.id.rv_students);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        numberPref = getSharedPreferences("number",MODE_PRIVATE);
        number = numberPref.getString("user_number",null);

        name = gcwDatabaseHelper.facultyDao().getName(number);
        dep = gcwDatabaseHelper.facultyDao().getDepartment(number);
        semReference = firebaseDatabase.getReference("Added Faculty").child(dep);
        studentReference = firebaseDatabase.getReference("Student").child(dep);

        rvStudent.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        semReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot semSnapshot : snapshot.getChildren()){
                    for (DataSnapshot facultySnapshot : semSnapshot.child("Faculties").getChildren()){
                        if (Objects.equals(facultySnapshot.getValue(String.class),name)){
                            sem.add(semSnapshot.getKey());
                        }
                    }
                }
                studentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (sem != null){
                            for (String semChild : sem){
                                for (DataSnapshot rollNoSnapshot : snapshot.child(semChild).getChildren()){
                                    if (rollNoSnapshot.hasChild("name")){
                                        studentList.add(rollNoSnapshot.child("name").getValue(String.class));
                                    }
                                    adapter.notifyItemRangeInserted(adapter.getItemCount(),studentList.size());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FacultyStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new RecyclerFacultyStudentAdapter(this,studentList);
        rvStudent.setAdapter(adapter);

    }
}