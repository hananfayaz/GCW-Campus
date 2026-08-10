package com.example.gcwcampus.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gcwcampus.entities.FacultyEntity;

import java.util.List;

@Dao
public interface FacultyDao {
    @Query("select * from faculty")
    List<FacultyEntity> getAllData();

    @Query("select name from faculty")
    List<String> getAllFacultyNames();

    @Query("select id from faculty where ph_no = :phNo")
    int getFacultyId(String phNo);

    @Query("SELECT name FROM faculty WHERE ph_no = :phNo")
    String getName(String phNo);

    @Query("select dept from faculty where ph_no = :phNo")
    String getDepartment(String phNo);

    @Query("select ph_no from faculty where id = :id")
    String getFacultyPhNo(int id);

    @Query("select password from faculty where ph_no = :phNo")
    String getFacultyPassword(String phNo);

    @Query("select email from faculty where ph_no = :phNo")
    String getFacultyEmail(String phNo);

    @Query("UPDATE faculty SET image_path = :imagePath WHERE id = :id")
    void updateImagePath(int id, String imagePath);

    @Query("SELECT image_path FROM faculty WHERE id = :id")
    String getImagePath(int id);

    @Insert
    void addFacultyInfo(FacultyEntity facultyEntity);

    @Update
    void updateFacultyInfo(FacultyEntity facultyEntity);

    @Delete
    void deleteFacultyInfo(FacultyEntity facultyEntity);
}
