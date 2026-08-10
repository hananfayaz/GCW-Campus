package com.example.gcwcampus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gcwcampus.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class DashboardActivity extends AppCompatActivity {

    boolean isChecked = false;
    String userRole;
    MenuInflater menuInflater;
    PopupMenu popupMenu;
    SharedPreferences userRolePref,userNamePref;
    RelativeLayout userProfile;
    ImageView userImage,menu;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        userProfile = findViewById(R.id.user_profile_layout);
        menu = findViewById(R.id.menu);

        userRolePref = getSharedPreferences("check_user_role",MODE_PRIVATE);
        userRole = userRolePref.getString("user_role",null);
        userNamePref = getSharedPreferences("check_user_name", MODE_PRIVATE);
        String name = userNamePref.getString("user_name", null);
        if (name == null) {
            userName.setText(R.string.user_not_found);
        } else {
            userName.setText(name);
        }

        switch (userRole){
            case "Faculty":
            case "Student":
                FirebaseMessaging.getInstance().subscribeToTopic("all");
                break;
        }

        popupMenu = new PopupMenu(this,menu);
        menuInflater = popupMenu.getMenuInflater();
        if (userRole.equals("Admin")){
            menuInflater.inflate(R.menu.admin_menu, popupMenu.getMenu());
        } else if (userRole.equals("Faculty")) {
            menuInflater.inflate(R.menu.faculty_menu, popupMenu.getMenu());
        }else {
            menuInflater.inflate(R.menu.student_menu, popupMenu.getMenu());
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (userRole.equals("Admin")){
                isChecked = adminMenu(item);
            }else if (userRole.equals("Faculty")){
                isChecked = facultyMenu(item);
            }else {
                isChecked = studentMenu(item);
            }
            return isChecked;
        });

        menu.setOnClickListener(v -> popupMenu.show());
        userImage.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

    }
    private void logout(){
        startActivity(new Intent(DashboardActivity.this, SignInActivity.class));
        finish();
    }

    private boolean studentMenu(MenuItem item){
        if (item.getItemId() == R.id.student_timetable_menu){
            startActivity(new Intent(this, TimetableStudentActivity.class));
        } else if (item.getItemId() == R.id.student_view_subject_menu) {
            startActivity(new Intent(this, SubjectStudentActivity.class));
        } else if (item.getItemId() == R.id.student_view_faculty_menu) {
            startActivity(new Intent(this, FacultiesActivity.class));
        } else {
            logout();
        }
        return true;
    }

    private boolean facultyMenu(MenuItem item){
        if (item.getItemId() == R.id.faculty_timetable_menu){
            startActivity(new Intent(this, TimetableFacultyActivity.class));
        } else if (item.getItemId() == R.id.faculty_sub_sem_menu) {
            startActivity(new Intent(this, SubjectFacultyActivity.class));
        } else if (item.getItemId() == R.id.faculty_students_menu) {
            startActivity(new Intent(this, FacultyStudentActivity.class));
        } else {
            logout();
        }
        return true;
    }

    private boolean adminMenu(MenuItem item){
        if (item.getItemId() == R.id.admin_add_timetable){
            startActivity(new Intent(this, AddTimetableActivity.class));
        } else if (item.getItemId() == R.id.admin_add_faculty_sem) {
            startActivity(new Intent(this, AddFacultyActivity.class));
        } else {
            logout();
        }
        return true;
    }
}