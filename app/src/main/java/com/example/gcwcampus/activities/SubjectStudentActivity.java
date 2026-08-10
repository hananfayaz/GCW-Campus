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
import com.example.gcwcampus.adapters.RecyclerSubjectAdapter;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubjectStudentActivity extends AppCompatActivity {

    ArrayList<String> subjectList = new ArrayList<>();
    String dep,sem,num;
    TextView tvSem, tvDepartment;
    RecyclerView rvSubject;
    RecyclerSubjectAdapter adapter;
    SharedPreferences numberPref;
    GcwDatabaseHelper gcwDatabaseHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subject_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvSem = findViewById(R.id.tv_sem);
        tvDepartment = findViewById(R.id.tv_dep);
        rvSubject = findViewById(R.id.rv_subject);

        rvSubject.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        numberPref = getSharedPreferences("number", MODE_PRIVATE);
        num = numberPref.getString("user_number", null);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        dep = gcwDatabaseHelper.studentDao().getStudentCourse(num);
        sem = gcwDatabaseHelper.studentDao().getStudentSem(num);

        tvDepartment.setText(dep);
        tvSem.setText(sem);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Timetable").child(dep).child(sem);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot daySnapshot : snapshot.getChildren()){
                    for (DataSnapshot periodSnapshot : daySnapshot.getChildren()){
                        subjectList.add(periodSnapshot.child("subject details").child("subject").getValue(String.class));
                    }
                }
                adapter.notifyItemRangeInserted(adapter.getItemCount(), subjectList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new RecyclerSubjectAdapter(this,subjectList);
        rvSubject.setAdapter(adapter);
    }
}