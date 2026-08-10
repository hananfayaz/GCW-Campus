package com.example.gcwcampus.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gcwcampus.entities.TimetableEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TimetableDao {
    @Query("select * from timetable")
    List<TimetableEntity> getAllDataFromTimetable();

    @Query("select sem from timetable where sem = :sem")
    String getSem(String sem);

    @Query("select department from timetable where department = :department")
    String getDepartment(String department);

    @Insert
    void addTimetable(TimetableEntity timetableEntity);
}
