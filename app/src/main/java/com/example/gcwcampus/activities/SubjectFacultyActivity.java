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
import com.example.gcwcampus.adapters.RecyclerSubjectFacultySemAdapter;
import com.example.gcwcampus.adapters.RecyclerSubjectFacultySubAdapter;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SubjectFacultyActivity extends AppCompatActivity {

    ArrayList<String> subjectList = new ArrayList<>();
    ArrayList<String> semList = new ArrayList<>();
    String name,number,department,faculty,sem;
    SharedPreferences numberPref;
    RecyclerView rvSem, rvSubject;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference semReference, subjectReference;
    GcwDatabaseHelper gcwDatabaseHelper;
    RecyclerSubjectFacultySemAdapter semAdapter;
    RecyclerSubjectFacultySubAdapter subAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subject_faculty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rvSem = findViewById(R.id.rv_sem);
        rvSubject =findViewById(R.id.rv_subject_faculty);

        numberPref = getSharedPreferences("number",MODE_PRIVATE);
        number = numberPref.getString("user_number",null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        name = gcwDatabaseHelper.facultyDao().getName(number);
        department = gcwDatabaseHelper.facultyDao().getDepartment(number);

        semReference = firebaseDatabase.getReference("Added Faculty").child(department);
        subjectReference = firebaseDatabase.getReference("Timetable").child(department);

        rvSem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvSubject.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        semReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot semSnapshot : snapshot.getChildren()){
                    if (semSnapshot.hasChild("Faculties")){
                        for (DataSnapshot facultySnapshot : semSnapshot.child("Faculties").getChildren()){
                            if (Objects.equals(facultySnapshot.getValue(String.class),name)){
                                faculty = facultySnapshot.getValue(String.class);
                                sem = semSnapshot.getKey();
                                semList.add(sem);
                            }
                        }
                    }
                }
                semAdapter.notifyItemRangeInserted(semAdapter.getItemCount(), semList.size());
                subjectReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (semList != null){
                            for (String semChild : semList){
                                for (DataSnapshot daySnapshot : snapshot.child(semChild).getChildren()){
                                    for (DataSnapshot periodSnapshot : daySnapshot.getChildren()){
                                        if (Objects.equals(periodSnapshot.child("subject details").child("faculty").getValue(String.class),name)){
                                            subjectList.add(periodSnapshot.child("subject details").child("subject").getValue(String.class));
                                        }
                                        subAdapter.notifyItemRangeInserted(subjectList.size(), subAdapter.getItemCount());
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SubjectFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        semAdapter = new RecyclerSubjectFacultySemAdapter(this,semList);
        rvSem.setAdapter(semAdapter);

        subAdapter = new RecyclerSubjectFacultySubAdapter(this,subjectList);
        rvSubject.setAdapter(subAdapter);
    }
}