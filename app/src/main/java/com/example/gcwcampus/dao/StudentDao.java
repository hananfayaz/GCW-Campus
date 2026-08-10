package com.example.gcwcampus.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gcwcampus.entities.StudentEntity;

import java.util.List;

@Dao
public interface StudentDao {
    @Query("select * from student")
    List<StudentEntity> getAllData();

    @Query("select id from student where ph_no = :phNo")
    int getStudentId(String phNo);

    @Query("select ph_no from student where id = :id")
    String getStudentPhNo(int id);

    @Query("select password from student where ph_no = :phNo")
    String getStudentPassword(String phNo);

    @Query("select email from student where ph_no = :phNo")
    String getStudentEmail(String phNo);

    @Query("UPDATE student SET image_path = :imagePath WHERE id = :id")
    void updateImagePath(int id, String imagePath);

    @Query("select sem from student where ph_no = :phNo")
    String getStudentSem(String phNo);

    @Query("select course from student where ph_no = :phNo")
    String getStudentCourse(String phNo);

    @Query("SELECT image_path FROM student WHERE id = :id")
    String getImagePath(int id);

    @Query("SELECT roll_no FROM student WHERE ph_no = :phNo")
    String getRollNo(String phNo);

    @Insert
    void addStudentInfo(StudentEntity studentEntity);

    @Update
    void updateStudentInfo(StudentEntity studentEntity);

    @Delete
    void deleteStudentInfo(StudentEntity studentEntity);
}
