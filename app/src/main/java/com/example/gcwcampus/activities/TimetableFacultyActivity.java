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
import com.example.gcwcampus.adapters.RecyclerFacultyTimetableAdapter;
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
import java.util.Objects;

public class TimetableFacultyActivity extends AppCompatActivity {

    ArrayList<RecyclerTimetableModel> timetableModel = new ArrayList<>();
    RecyclerView timetable;
    RecyclerFacultyTimetableAdapter adapter;
    SharedPreferences numberPref;
    RecyclerTimetableModel recyclerTimetableModel = new RecyclerTimetableModel();
    String name, number, department;
    GcwDatabaseHelper gcwDatabaseHelper;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timetable_faculty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        timetable = findViewById(R.id.rv_timetable);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Timetable");

        timetable.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        numberPref = getSharedPreferences("number",MODE_PRIVATE);
        number = numberPref.getString("user_number",null);

        department = gcwDatabaseHelper.facultyDao().getDepartment(number);
        name = gcwDatabaseHelper.facultyDao().getName(number);

        databaseReference.child(department).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot semSnapshot : snapshot.getChildren()){
                    for (DataSnapshot daySnapshot : semSnapshot.getChildren()){
                        for (DataSnapshot periodSnapshot : daySnapshot.getChildren()){
                            if (Objects.equals(periodSnapshot.child("subject details").child("faculty").getValue(String.class), name)){
                                recyclerTimetableModel.setPeriod(periodSnapshot.getKey());
                                recyclerTimetableModel.setDay(daySnapshot.getKey());
                                recyclerTimetableModel.setSubject(periodSnapshot.child("subject details").child("subject").getValue(String.class));
                                recyclerTimetableModel.setStartTime(periodSnapshot.child("subject details").child("startTime").getValue(String.class));
                                recyclerTimetableModel.setEndTime(periodSnapshot.child("subject details").child("endTime").getValue(String.class));
                                recyclerTimetableModel.setDepartment(department);

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    DateTimeFormatter inputTime = DateTimeFormatter.ofPattern("[H:m][HH:mm][H:mm:ss][HH:mm:ss]");
                                    DateTimeFormatter outputTime = DateTimeFormatter.ofPattern("hh:mm a");

                                    LocalTime startTimeValue = LocalTime.parse(recyclerTimetableModel.getStartTime(), inputTime);
                                    String startTime12Hour = startTimeValue.format(outputTime);
                                    LocalTime endTimeValue = LocalTime.parse(recyclerTimetableModel.getEndTime(), inputTime);
                                    String endTime12Hour = endTimeValue.format(outputTime);

                                    recyclerTimetableModel.setStartTime(startTime12Hour);
                                    recyclerTimetableModel.setEndTime(endTime12Hour);

                                }
                                timetableModel.add(recyclerTimetableModel);
                                adapter.notifyItemRangeChanged(adapter.getItemCount(), timetableModel.size());
                                recyclerTimetableModel = new RecyclerTimetableModel();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TimetableFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new RecyclerFacultyTimetableAdapter(this, timetableModel);
        timetable.setAdapter(adapter);
    }

}