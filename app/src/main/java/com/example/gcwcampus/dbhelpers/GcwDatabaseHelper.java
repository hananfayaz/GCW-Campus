package com.example.gcwcampus.dbhelpers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gcwcampus.dao.AdminDao;
import com.example.gcwcampus.dao.FacultyDao;
import com.example.gcwcampus.dao.StudentDao;
import com.example.gcwcampus.dao.TimetableDao;
import com.example.gcwcampus.entities.AdminEntity;
import com.example.gcwcampus.entities.FacultyEntity;
import com.example.gcwcampus.entities.StudentEntity;
import com.example.gcwcampus.entities.TimetableEntity;

@Database(entities = {AdminEntity.class, FacultyEntity.class, StudentEntity.class, TimetableEntity.class},exportSchema = false, version = 3)
public abstract class GcwDatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "gcw_db";
    private static GcwDatabaseHelper instance;
    public static synchronized GcwDatabaseHelper getDatabase(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context, GcwDatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AdminDao adminDao();
    public abstract FacultyDao facultyDao();
    public abstract StudentDao studentDao();
    public abstract TimetableDao timetableDao();
}
