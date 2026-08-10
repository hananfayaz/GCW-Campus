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
import com.example.gcwcampus.adapters.RecyclerStudentTimetableAdapter;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.example.gcwcampus.models.RecyclerTimetableModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimetableStudentActivity extends AppCompatActivity {

    ArrayList<RecyclerTimetableModel> timetableModel = new ArrayList<>();
    RecyclerView timetable;
    RecyclerStudentTimetableAdapter adapter;
    SharedPreferences numberPref;
    RecyclerTimetableModel recyclerTimetableModel;
    String sem, number, department;
    GcwDatabaseHelper gcwDatabaseHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        timetable = findViewById(R.id.rv_timetable);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Timetable");
        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);

        timetable.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        numberPref = getSharedPreferences("number", MODE_PRIVATE);
        number = numberPref.getString("user_number", null);

        sem = gcwDatabaseHelper.studentDao().getStudentSem(number);
        department = gcwDatabaseHelper.studentDao().getStudentCourse(number);
        loadTimetable(department,sem);

        adapter = new RecyclerStudentTimetableAdapter(this, timetableModel);
        timetable.setAdapter(adapter);

    }
    private void loadTimetable(String department, String sem){
        databaseReference.child(department).child(sem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                    String day = daySnapshot.getKey();
                    for (DataSnapshot subjectSnapshot : daySnapshot.getChildren()) {
                        String period = subjectSnapshot.getKey();
                        String subject = subjectSnapshot.child("subject details").child("subject").getValue(String.class);
                        String startTime = subjectSnapshot.child("subject details").child("startTime").getValue(String.class);
                        String endTime = subjectSnapshot.child("subject details").child("endTime").getValue(String.class);
                        String faculty = subjectSnapshot.child("subject details").child("faculty").getValue(String.class);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter inputTime = DateTimeFormatter.ofPattern("[H:m][HH:mm][H:mm:ss][HH:mm:ss]");
                            DateTimeFormatter outputTime = DateTimeFormatter.ofPattern("hh:mm a");

                            LocalTime startTimeValue = LocalTime.parse(startTime, inputTime);
                            String startTime12Hour = startTimeValue.format(outputTime);
                            LocalTime endTimeValue = LocalTime.parse(endTime, inputTime);
                            String endTime12Hour = endTimeValue.format(outputTime);

                            recyclerTimetableModel = new RecyclerTimetableModel(subject, startTime12Hour, endTime12Hour, faculty, period, day);
                            timetableModel.add(recyclerTimetableModel);
                        } else {
                            recyclerTimetableModel = new RecyclerTimetableModel(subject, startTime, endTime, faculty, period, day);
                            timetableModel.add(recyclerTimetableModel);
                        }
                    }
                }
                adapter.notifyItemRangeInserted(adapter.getItemCount(), timetableModel.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TimetableStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}