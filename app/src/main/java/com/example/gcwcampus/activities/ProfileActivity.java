package com.example.gcwcampus.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.gcwcampus.R;
import com.example.gcwcampus.dbhelpers.GcwDatabaseHelper;
import com.example.gcwcampus.entities.AdminEntity;
import com.example.gcwcampus.entities.FacultyEntity;
import com.example.gcwcampus.entities.StudentEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    int adminId, facultyId, studentId;
    String userRole, imgPath, number,fileName;
    ImageView profileImage;
    TextView userName, txtViewSem, txtViewDepartment, txtViewRollNo, txtViewRegNo , userPhNo, userAddress, userDob, userEmail, userGender, userSem, userDepartment, userRollNo, userRegNo;
    ActivityResultLauncher<String> pickImageLauncher;
    GcwDatabaseHelper gcwDatabaseHelper;
    SharedPreferences userRolePref, numberPref;
    List<AdminEntity> adminProfile = new ArrayList<>();
    List<FacultyEntity> facultyProfile = new ArrayList<>();
    List<StudentEntity> studentProfile = new ArrayList<>();
    Bitmap bitmap;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.name);
        txtViewSem = findViewById(R.id.sem_textview);
        txtViewDepartment = findViewById(R.id.dep_textview);
        txtViewRollNo = findViewById(R.id.roll_no_textview);
        txtViewRegNo = findViewById(R.id.reg_no_textview);
        userPhNo = findViewById(R.id.user_ph_no);
        userAddress = findViewById(R.id.user_address);
        userDob = findViewById(R.id.user_dob);
        userEmail = findViewById(R.id.user_email);
        userGender = findViewById(R.id.user_gender);
        userSem = findViewById(R.id.user_sem);
        userDepartment = findViewById(R.id.user_department);
        userRollNo = findViewById(R.id.user_roll_no);
        userRegNo = findViewById(R.id.user_reg_no);

        gcwDatabaseHelper = GcwDatabaseHelper.getDatabase(this);
        userRolePref = getSharedPreferences("check_user_role", MODE_PRIVATE);
        userRole = userRolePref.getString("user_role", null);
        numberPref = getSharedPreferences("number", MODE_PRIVATE);
        number = numberPref.getString("user_number", null);

        adminId = gcwDatabaseHelper.adminDao().getAdminId(number);
        facultyId = gcwDatabaseHelper.facultyDao().getFacultyId(number);
        studentId = gcwDatabaseHelper.studentDao().getStudentId(number);

        switch (Objects.requireNonNull(userRole)){
            case "Admin":
                String adminImagePath = gcwDatabaseHelper.adminDao().getImagePath(adminId);
                if (adminImagePath != null){
                    File imgFile = new File(adminImagePath);
                    Glide.with(this).load(imgFile).into(profileImage);
                }
                txtViewSem.setVisibility(View.GONE);
                userSem.setVisibility(View.GONE);
                txtViewDepartment.setVisibility(View.GONE);
                userDepartment.setVisibility(View.GONE);
                txtViewRollNo.setVisibility(View.GONE);
                userRollNo.setVisibility(View.GONE);
                txtViewRegNo.setVisibility(View.GONE);
                userRegNo.setVisibility(View.GONE);
                adminProfile = gcwDatabaseHelper.adminDao().getAllData();
                setUserDetails(userRole,adminProfile.size());
                break;
            case "Faculty":
                String facultyImagePath = gcwDatabaseHelper.facultyDao().getImagePath(facultyId);
                if (facultyImagePath != null){
                    File imgFile = new File(facultyImagePath);
                    Glide.with(this).load(imgFile).into(profileImage);
                }
                txtViewSem.setVisibility(View.GONE);
                userSem.setVisibility(View.GONE);
                txtViewRollNo.setVisibility(View.GONE);
                userRollNo.setVisibility(View.GONE);
                txtViewRegNo.setVisibility(View.GONE);
                userRegNo.setVisibility(View.GONE);
                facultyProfile = gcwDatabaseHelper.facultyDao().getAllData();
                setUserDetails(userRole,facultyProfile.size());
                break;
            case "Student":
                String studentImagePath = gcwDatabaseHelper.studentDao().getImagePath(studentId);
                if (studentImagePath != null){
                    File imgFile = new File(studentImagePath);
                    Glide.with(this).load(imgFile).into(profileImage);
                }
                studentProfile = gcwDatabaseHelper.studentDao().getAllData();
                setUserDetails(userRole,studentProfile.size());
                break;
        }

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), o -> {
            if (o != null){
                profileImage.setImageURI(o);
                new Thread(() -> {
                    try (InputStream inputStream = getContentResolver().openInputStream(o)){
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        File dir = this.getFilesDir();
                        switch (userRole){
                            case "Admin":
                                fileName = "profile_"+userRole+System.currentTimeMillis()+"_"+adminId+"_"+".jpg";
                                break;
                                case "Faculty":
                                fileName = "profile_"+userRole+System.currentTimeMillis()+"_"+facultyId+"_"+".jpg";
                                break;
                            case "Student":
                                fileName = "profile_"+userRole+System.currentTimeMillis()+"_"+studentId+"_"+".jpg";
                                break;
                        }
                        imageFile = new File(dir,fileName);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            try (OutputStream outputStream = Files.newOutputStream(imageFile.toPath())){
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
                                outputStream.flush();
                                imgPath = imageFile.getAbsolutePath();
                                switch (userRole){
                                    case "Admin":
                                        gcwDatabaseHelper.adminDao().updateImagePath(adminId,imgPath);
                                        break;
                                    case "Faculty":
                                        gcwDatabaseHelper.facultyDao().updateImagePath(facultyId,imgPath);
                                        break;
                                    case "Student":
                                        gcwDatabaseHelper.studentDao().updateImagePath(studentId,imgPath);
                                        break;
                                }
                            }
                        }
                    }catch (IOException e){
                        runOnUiThread(() -> Toast.makeText(this,"Error saving image", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });

        profileImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
    }
    private void setUserDetails(String user, int arraySize){
        for (int i = 0; i < arraySize; i++) {
            switch (user){
                case "Admin":
                    userName.setText(adminProfile.get(i).getName());
                    userPhNo.setText(adminProfile.get(i).getPhNo());
                    userAddress.setText(adminProfile.get(i).getAddress());
                    userDob.setText(adminProfile.get(i).getDob());
                    userEmail.setText(adminProfile.get(i).getEmail());
                    userGender.setText(adminProfile.get(i).getGender());
                    break;
                case "Faculty":
                    userName.setText(facultyProfile.get(i).getName());
                    userPhNo.setText(facultyProfile.get(i).getPhNo());
                    userAddress.setText(facultyProfile.get(i).getAddress());
                    userDob.setText(facultyProfile.get(i).getDob());
                    userEmail.setText(facultyProfile.get(i).getEmail());
                    userGender.setText(facultyProfile.get(i).getGender());
                    userDepartment.setText(facultyProfile.get(i).getDepartment());
                    break;
                case "Student":
                    userName.setText(studentProfile.get(i).getName());
                    userPhNo.setText(studentProfile.get(i).getPhNo());
                    userAddress.setText(studentProfile.get(i).getAddress());
                    userDob.setText(studentProfile.get(i).getDob());
                    userEmail.setText(studentProfile.get(i).getEmail());
                    userGender.setText(studentProfile.get(i).getGender());
                    userDepartment.setText(studentProfile.get(i).getCourse());
                    userSem.setText(studentProfile.get(i).getSemester());
                    userRollNo.setText(studentProfile.get(i).getRollNo());
                    userRegNo.setText(studentProfile.get(i).getRegNo());
                    break;
            }
        }
    }
}