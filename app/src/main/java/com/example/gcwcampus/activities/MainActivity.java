package com.example.gcwcampus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gcwcampus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences loginPreferences,userProfilePreferences,userRolePreferences;
    ImageView splashLogo;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        splashLogo = findViewById(R.id.splash_logo);

        firebaseAuth = FirebaseAuth.getInstance();

        loginPreferences = getSharedPreferences("login",MODE_PRIVATE);
        boolean loginCheck = loginPreferences.getBoolean("is_login",false);
        userProfilePreferences = getSharedPreferences("check_on_profile",MODE_PRIVATE);
        boolean profileSubmitted = userProfilePreferences.getBoolean("is_submitted",false);
        userRolePreferences = getSharedPreferences("check_user_role",MODE_PRIVATE);
        String userRole = userRolePreferences.getString("user_role",null);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        Glide.with(this).load(R.drawable.splash_logo).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(MainActivity.this, "Image Not Loaded", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    if (firebaseUser != null) {
                        if (loginCheck && profileSubmitted) {
                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                            finish();
                        } else if (!loginCheck && profileSubmitted) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                            finish();
                        } else if (!profileSubmitted && loginCheck) {
                            if (userRole != null){
                                if (Objects.equals(userRole, "Admin")) {
                                    startActivity(new Intent(MainActivity.this, AdminCreateProfileActivity.class));
                                    finish();
                                } else if (Objects.equals(userRole, "Faculty")) {
                                    startActivity(new Intent(MainActivity.this, FacultyCreateProfileActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(MainActivity.this, StudentCreateProfileActivity.class));
                                    finish();
                                }
                            }
                        } else {
                            startActivity(new Intent(MainActivity.this, GetStartedActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(MainActivity.this, GetStartedActivity.class));
                        finish();
                    }
                },2000);
                return false;
            }
        }).into(splashLogo);


    }
}